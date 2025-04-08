package com.blog.user.infrastructure;

import com.blog.user.domain.User;
import com.blog.user.domain.UserRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import javax.sql.DataSource;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource;

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public User findById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (email, password, name, nickname, birth_date, profile_image, introduction, auth_method, created_at, updated_at, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getNickname());
            ps.setString(5, user.getBirthDate());
            ps.setString(6, user.getProfileImage());
            ps.setString(7, user.getIntroduction());
            ps.setString(8, user.getAuthMethod());
            ps.setTimestamp(9, user.getCreatedAt());
            ps.setTimestamp(10, user.getUpdatedAt());
            ps.setBoolean(11, user.isActive());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                user.setUserId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET nickname = ?, profile_image = ?, introduction = ?, updated_at = ? WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getNickname());
            ps.setString(2, user.getProfileImage());
            ps.setString(3, user.getIntroduction());
            ps.setTimestamp(4, user.getUpdatedAt());
            ps.setLong(5, user.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void softDelete(Long userId) {
        String sql = "UPDATE users SET is_active = false WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("nickname"),
                rs.getString("birth_date"),
                rs.getString("profile_image"),
                rs.getString("introduction"),
                rs.getString("auth_method"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getBoolean("is_active")
        );
    }
}

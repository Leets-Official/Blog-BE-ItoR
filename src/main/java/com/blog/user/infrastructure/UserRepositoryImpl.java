package com.blog.user.infrastructure;

import com.blog.user.domain.User;
import com.blog.user.domain.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (email, password, name, nickname, birth_date, profile_image, introduction, auth_method, created_at, updated_at, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getNickname(),
                user.getBirthDate(),
                user.getProfileImage(),
                user.getIntroduction(),
                user.getAuthMethod(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isActive()
        );
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, userRowMapper())
                .stream().findFirst().orElse(null);
    }

    @Override
    public User findById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, userRowMapper())
                .stream().findFirst().orElse(null);
    }

    @Override
    public void update(Long userId, String nickname, String profileImage, Timestamp updatedAt) {
        String sql = "UPDATE users SET nickname = ?, profile_image = ?, updated_at = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, nickname, profileImage, updatedAt, userId);
    }

    @Override
    public void softDelete(Long userId) {
        String sql = "UPDATE users SET is_active = false WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
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

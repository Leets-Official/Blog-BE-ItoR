package com.blog.token.infrastructure;

import com.blog.token.domain.Token;
import com.blog.token.domain.TokenRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private final DataSource dataSource;

    public TokenRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Token token) {
        String sql = "INSERT INTO tokens (user_id, refresh_token, expires_at, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, token.getUserId());
            ps.setString(2, token.getRefreshToken());
            ps.setTimestamp(3, token.getExpiresAt());
            ps.setTimestamp(4, token.getCreatedAt());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                token.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Token findByUserId(Long userId) {
        String sql = "SELECT * FROM tokens WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToToken(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM tokens WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Token mapRowToToken(ResultSet rs) throws SQLException {
        return new Token(
                rs.getLong("user_id"),
                rs.getString("refresh_token"),
                rs.getTimestamp("expires_at"),
                rs.getTimestamp("created_at")
        );
    }
}

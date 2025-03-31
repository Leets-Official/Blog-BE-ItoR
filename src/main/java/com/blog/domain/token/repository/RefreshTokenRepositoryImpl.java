package com.blog.domain.token.repository;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.blog.domain.token.domain.RefreshToken;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

	private final JdbcTemplate jdbc;

	public RefreshTokenRepositoryImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Optional<RefreshToken> findByUserId(int userId) {
		String sql = "SELECT * FROM refresh_tokens WHERE userId = ?";

		return jdbc.query(sql, (rs, row) -> RefreshToken.of(
			rs.getInt("userId"),
			rs.getString("refreshToken"),
			rs.getTimestamp("expiredAt").toLocalDateTime(),
			rs.getTimestamp("createdAt").toLocalDateTime()
		), userId).stream().findFirst();
	}

	@Override
	public void save(RefreshToken token) {
		deleteByUserId(token.getUserId());
		String sql = "INSERT INTO refresh_tokens (userId, refreshToken, expiredAt, createdAt) VALUES (?, ?, ?, ?)";
		jdbc.update(sql,
			token.getUserId(),
			token.getRefreshToken(),
			Timestamp.valueOf(token.getExpiredAt()),
			Timestamp.valueOf(token.getCreatedAt()));
	}

	@Override
	public void deleteByUserId(int userId) {
		jdbc.update("DELETE FROM refresh_tokens WHERE userId = ?", userId);
	}
}

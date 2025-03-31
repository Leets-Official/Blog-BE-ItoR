package com.blog.domain.user.repository;

import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.domain.UserType;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final JdbcTemplate jdbc;

	public UserRepositoryImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		String sql = "SELECT * FROM users WHERE email = ?";
		return jdbc.query(sql, userRowMapper(), email)
			.stream()
			.findFirst();
	}

	@Override
	public boolean existsByEmail(String email) {
		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
		Integer count = jdbc.queryForObject(sql, Integer.class, email);
		return count != null && count > 0;
	}

	@Override
	public void save(User user) {
		String sql = "INSERT INTO users (name, nickName, email, password, userType, birthDate, introduce, profileImageUrl, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbc.update(sql,
			user.getName(),
			user.getNickName(),
			user.getEmail(),
			user.getPassword(),
			user.getUserType().name(),
			user.getBirthDate(),
			user.getIntroduce(),
			user.getProfileImageUrl(),
			user.getCreatedAt()
		);

	}

	private RowMapper<User> userRowMapper() {
		return (rs, rowNum) -> new User(
			rs.getInt("userId"),
			rs.getString("name"),
			rs.getString("nickName"),
			rs.getString("email"),
			rs.getString("password"),
			UserType.valueOf(rs.getString("userType")),
			rs.getDate("birthDate").toLocalDate(),
			rs.getString("introduce"),
			rs.getString("profileImageUrl"),
			rs.getTimestamp("createdAt").toLocalDateTime()
		);
		}
	}


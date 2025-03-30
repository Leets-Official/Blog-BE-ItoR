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
		return (rs, rowNum) -> {
			User user = new User();
			user.setUserId(rs.getInt("userId"));
			user.setName(rs.getString("name"));
			user.setNickName(rs.getString("nickName"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setUserType(UserType.valueOf(rs.getString("userType")));
			user.setBirthDate(rs.getDate("birthDate").toLocalDate());
			user.setIntroduce(rs.getString("introduce"));
			user.setProfileImageUrl(rs.getString("profileImageUrl"));
			user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
			return user;
		};
	}

}

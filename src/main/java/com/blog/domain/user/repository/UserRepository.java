package com.blog.domain.user.repository;

import com.blog.domain.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
  private final JdbcTemplate jdbcTemplate;

  public UserRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(User user) {
    String sql = "INSERT INTO user (id, nickname, password, email, profile_image_url) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        user.getId().toString(),
        user.getNickname(),
        user.getPassword(),
        user.getEmail(),
        user.getProfileImageUrl()
    );
  }

  public Optional<User> findByEmail(String email) {
    String sql = "SELECT * FROM user WHERE email = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper(), email);
    return users.stream().findFirst();
  }

  public List<User> findAll() {
    String sql = "SELECT * FROM user";
    return jdbcTemplate.query(sql, userRowMapper());
  }

  public Optional<User> findById(String id) {
    String sql = "SELECT * FROM user WHERE id = ?";
    List<User> users = jdbcTemplate.query(sql, userRowMapper(), id);
    return users.stream().findFirst();
  }

  private RowMapper<User> userRowMapper() {
    return (rs, rowNum) -> new User(
        UUID.fromString(rs.getString("id")),
        rs.getString("nickname"),
        rs.getString("password"),
        rs.getString("email"),
        rs.getString("profile_image_url"),
        rs.getTimestamp("created_at").toLocalDateTime(),
        rs.getTimestamp("updated_at").toLocalDateTime()
    );
  }
}

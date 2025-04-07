package com.blog.domain.post.repository;

import com.blog.domain.post.domain.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostRepository {
  private final JdbcTemplate jdbcTemplate;

  public PostRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(Post post) {
    String sql = "INSERT INTO post (id, title, content, author_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        post.getId().toString(),
        post.getTitle(),
        post.getContent(),
        post.getAuthorId().toString(),
        Timestamp.valueOf(post.getCreatedAt()),
        Timestamp.valueOf(post.getUpdatedAt())
    );
  }

  public Optional<Post> findById(UUID id) {
    String sql = "SELECT * FROM post WHERE id = ?";
    List<Post> result = jdbcTemplate.query(sql, postRowMapper(), id.toString());
    return result.stream().findFirst();
  }

  public List<Post> findAll() {
    String sql = "SELECT * FROM post";
    return jdbcTemplate.query(sql, postRowMapper());
  }

  public void update(Post post) {
    String sql = "UPDATE post SET title = ?, content = ?, updated_at = ? WHERE id = ?";
    jdbcTemplate.update(sql,
        post.getTitle(),
        post.getContent(),
        Timestamp.valueOf(post.getUpdatedAt()),
        post.getId().toString()
    );
  }

  public void deleteById(UUID id) {
    String sql = "DELETE FROM post WHERE id = ?";
    jdbcTemplate.update(sql, id.toString());
  }

  private RowMapper<Post> postRowMapper() {
    return (rs, rowNum) -> new Post(
        UUID.fromString(rs.getString("id")),
        rs.getString("title"),
        rs.getString("content"),
        UUID.fromString(rs.getString("author_id")),
        rs.getTimestamp("created_at").toLocalDateTime(),
        rs.getTimestamp("updated_at").toLocalDateTime()
    );
  }
}

package com.blog.domain.comment.repository;

import com.blog.domain.comment.domain.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CommentRepository {
  private final JdbcTemplate jdbcTemplate;

  public CommentRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(Comment comment) {
    String sql = "INSERT INTO comment (id, content, author_id, post_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        comment.getId().toString(),
        comment.getContent(),
        comment.getAuthorId().toString(),
        comment.getPostId().toString(),
        Timestamp.valueOf(comment.getCreatedAt()),
        Timestamp.valueOf(comment.getUpdatedAt())
    );
  }

  public Optional<Comment> findById(UUID id) {
    String sql = "SELECT * FROM comment WHERE id = ?";
    List<Comment> result = jdbcTemplate.query(sql, commentRowMapper(), id.toString());
    return result.stream().findFirst();
  }

  public List<Comment> findByPostId(UUID postId) {
    String sql = "SELECT * FROM comment WHERE post_id = ?";
    return jdbcTemplate.query(sql, commentRowMapper(), postId.toString());
  }

  public void update(Comment comment) {
    String sql = "UPDATE comment SET content = ?, updated_at = ? WHERE id = ?";
    jdbcTemplate.update(sql,
        comment.getContent(),
        Timestamp.valueOf(comment.getUpdatedAt()),
        comment.getId().toString()
    );
  }

  public void deleteById(UUID id) {
    String sql = "DELETE FROM comment WHERE id = ?";
    jdbcTemplate.update(sql, id.toString());
  }

  private RowMapper<Comment> commentRowMapper() {
    return (rs, rowNum) -> new Comment(
        UUID.fromString(rs.getString("id")),
        rs.getString("content"),
        UUID.fromString(rs.getString("author_id")),
        UUID.fromString(rs.getString("post_id")),
        rs.getTimestamp("created_at").toLocalDateTime(),
        rs.getTimestamp("updated_at").toLocalDateTime()
    );
  }
}

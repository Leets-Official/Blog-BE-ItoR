package com.blog.domain.image.repository;

import com.blog.domain.image.domain.Image;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ImageRepository {
  private final JdbcTemplate jdbcTemplate;

  public ImageRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(Image image) {
    String sql = "INSERT INTO image (id, original_name, stored_name, url, created_at) VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        image.getId().toString(),
        image.getOriginalName(),
        image.getStoredName(),
        image.getUrl(),
        Timestamp.valueOf(image.getCreatedAt()));
  }

  public Optional<Image> findById(UUID id) {
    String sql = "SELECT * FROM image WHERE id = ?";
    return jdbcTemplate.query(sql, rowMapper(), id.toString())
        .stream().findFirst();
  }

  private RowMapper<Image> rowMapper() {
    return (rs, rowNum) -> new Image(
        UUID.fromString(rs.getString("id")),
        rs.getString("original_name"),
        rs.getString("stored_name"),
        rs.getString("url"),
        rs.getTimestamp("created_at").toLocalDateTime()
    );
  }
}

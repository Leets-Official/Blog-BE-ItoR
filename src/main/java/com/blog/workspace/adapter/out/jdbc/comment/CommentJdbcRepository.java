package com.blog.workspace.adapter.out.jdbc.comment;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CommentJdbc save(CommentJdbc commentJdbc) {
        String sql = "INSERT INTO comment (post_id, user_id, content, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                commentJdbc.getPostId(),
                commentJdbc.getUserId(),
                commentJdbc.getContent(),
                Timestamp.valueOf(commentJdbc.getCreatedAt()),
                Timestamp.valueOf(commentJdbc.getUpdatedAt())
        );

        return commentJdbc;
    }

    public Optional<CommentJdbc> findById(Long id) {
        String sql = "SELECT * FROM comment WHERE id = ?";
        return jdbcTemplate.query(sql, CommentJdbcRowMapper(), id)
                .stream()
                .findFirst();
    }


    public List<CommentJdbc> findByPostId(Long postId) {
        String sql = "SELECT * FROM comment WHERE post_id = ? ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, CommentJdbcRowMapper(), postId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM comment WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public CommentJdbc update(CommentJdbc commentJdbc) {
        String sql = "UPDATE comment SET content = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                commentJdbc.getContent(),
                Timestamp.valueOf(commentJdbc.getUpdatedAt()),
                commentJdbc.getId()
        );

        return commentJdbc;
    }

    private RowMapper<CommentJdbc> CommentJdbcRowMapper() {
        return (rs, rowNum) -> CommentJdbc.fromDB(
                rs.getLong("id"),
                rs.getLong("post_id"),
                rs.getLong("user_id"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}

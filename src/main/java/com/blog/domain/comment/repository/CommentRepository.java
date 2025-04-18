package com.blog.domain.comment.repository;

import com.blog.domain.comment.domain.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 댓글 저장
    public void save(Comment comment) {
        String sql = "INSERT INTO comment (user_id, post_id, content, created_at) VALUES (?, ?, ?, NOW())";
        jdbcTemplate.update(sql, comment.getUserId(), comment.getPostId(), comment.getContent());
    }

    // 댓글 수정
    public void update(Comment comment) {
        String sql = "UPDATE comment SET content = ?, updated_at = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, comment.getContent(), comment.getId());
    }

    // 댓글 삭제
    public int deleteByCommentId(Long commentId) {
        String sql = "DELETE FROM comment WHERE id = ?";
        return jdbcTemplate.update(sql, commentId);
    }

    // 모든 댓글 삭제
    public void deleteAllByPostId(Long postId) {
        String sql = "DELETE FROM comment WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    // 댓글 조회
    public Comment findByCommentId(Long commentId) {
        String sql = "SELECT * FROM comment WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            Long userId = rs.getLong("user_id");
            Long postId = rs.getLong("post_id");
            String content = rs.getString("content");
            return new Comment(id, userId, postId, content);
        }, commentId);

    }

    // 댓글 전체 조회
    public List<Comment> findAllByPostId(Long postId) {
        String sql = "SELECT id, user_id, post_id, content, created_at, updated_at FROM comment WHERE post_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            Long userId = rs.getLong("user_id");
            String content = rs.getString("content");
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null;
            return new Comment(id, userId, postId, content, createdAt, updatedAt);
        }, postId);
    }

}

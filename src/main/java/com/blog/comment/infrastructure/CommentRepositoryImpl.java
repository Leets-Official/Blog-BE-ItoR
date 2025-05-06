package com.blog.comment.infrastructure;

import com.blog.comment.domain.Comment;
import com.blog.comment.domain.CommentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Comment comment) {
        String sql = "INSERT INTO comments (post_id, user_id, content, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"comment_id"});
            ps.setInt(1, comment.getPostId());
            ps.setInt(2, comment.getUserId());
            ps.setString(3, comment.getContent());
            ps.setTimestamp(4, comment.getCreatedAt());
            return ps;
        }, keyHolder);


        comment.setCommentId(keyHolder.getKey().intValue());
    }

    @Override
    public List<Comment> findByPostId(int postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ?";
        return jdbcTemplate.query(sql, commentRowMapper(), postId);
    }

    @Override
    public Comment findById(int postId, int commentId) {
        String sql = "SELECT * FROM comments WHERE post_id = ? AND comment_id = ?";
        return jdbcTemplate.queryForObject(sql, commentRowMapper(), postId, commentId);
    }

    @Override
    public void update(int commentId, int postId, int userId, String content, Timestamp updatedAt) {
        String sql = "UPDATE comments SET content = ?, updated_at = ? WHERE comment_id = ? AND post_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, content, updatedAt, commentId, postId, userId);
    }

    @Override
    public void delete(int postId, int commentId, int userId) {
        String sql = "DELETE FROM comments WHERE post_id = ? AND comment_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, postId, commentId, userId);
    }

    private RowMapper<Comment> commentRowMapper() {
        return (rs, rowNum) -> new Comment(
                rs.getInt("comment_id"),
                rs.getInt("post_id"),
                rs.getInt("user_id"),
                rs.getString("content"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}

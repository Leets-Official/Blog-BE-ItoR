package com.blog.domain.comment.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.blog.domain.comment.domain.Comment;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

	private	final JdbcTemplate jdbcTemplate;

	public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<Comment> findById(int commentId) {
		String sql = "SELECT * FROM comments WHERE commentId = ?";
		List<Comment> list = jdbcTemplate.query(sql, (rs, rn) -> mapRow(rs), commentId);
		return list.isEmpty()
			? Optional.empty()
			: Optional.of(list.get(0));
	}


	@Override
	public void save(Comment comment) {
		String sql = """
    INSERT INTO comments
      (userId, postId, content)
    VALUES (?,?,?)
    """;
		jdbcTemplate.update(
			sql,
			comment.getUserId(),
			comment.getPostId(),
			comment.getContent()
		);
	}

	@Override
	public List<Comment> findByPostId(int postId) {
		String sql = "SELECT * FROM comments WHERE postId = ?";
		return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs), postId);

	}

	@Override
	public boolean update(Comment comment) {
		String sql = """
    UPDATE comments
       SET content    = ?,
           modifiedAt = ?
     WHERE commentId  = ?
    """;
		return jdbcTemplate.update(
			sql,
			comment.getContent(),
			Timestamp.valueOf(comment.getModifiedAt()),
			comment.getCommentId()
		) > 0;
	}

	@Override
	public boolean delete(int commentId) {
		String sql = "DELETE FROM comments WHERE commentId = ?";
		return jdbcTemplate.update(sql, commentId) > 0;

	}

	private Comment mapRow(ResultSet rs) throws SQLException {
		return new Comment(
			rs.getInt("commentId"),
			rs.getInt("userId"),
			rs.getInt("postId"),
			rs.getString("content"),
			rs.getTimestamp("createdAt").toLocalDateTime(),
			rs.getTimestamp("modifiedAt").toLocalDateTime(),
			rs.getTimestamp("deletedAt") != null ? rs.getTimestamp("deletedAt").toLocalDateTime() : null
		);
	}

}

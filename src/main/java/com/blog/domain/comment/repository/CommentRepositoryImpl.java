package com.blog.domain.comment.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
	public void save(Comment comment) {
		String sql = "INSERT INTO comment (userId, postId, content, createdAt, modifiedAt ) VALUES (?,?,?,?,?))";
		jdbcTemplate.update(sql,
			comment.getUserId(),
			comment.getPostId(),
			comment.getContent(),
			comment.getCreatedAt(),
			comment.getModifiedAt());

	}

	@Override
	public List<Comment> findByPostId(int postId) {
		String sql = "SELECT * FROM comment WHERE postId = ?";
		return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs), postId);

	}

	@Override
	public boolean update(Comment comment) {
		String sql = "UPDATE comment SET content = ? WHERE commentId =?";
		return jdbcTemplate.update(sql, comment.getContent(), comment.getCommentId()) > 0;
	}

	@Override
	public boolean delete(int commentId) {
		String sql = "DELETE FROM comment WHERE commentId = ?";
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

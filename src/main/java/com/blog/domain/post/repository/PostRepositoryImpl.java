package com.blog.domain.post.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.blog.domain.post.domain.Post;

@Repository
public class PostRepositoryImpl implements PostRepository {

	private final JdbcTemplate jdbcTemplate;

	public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean insert(Post post) {
		String sql = """
			INSERT INTO posts (userId, title, content, createdAt, modifiedAt)
			VALUES (?, ?, ?, ?, ?)
			""";

		int result = jdbcTemplate.update(sql,post.getUserId(),post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
		return result == 1;
	}

	@Override
	public Post findById(int postId) {
		String sql = "SELECT * FROM posts WHERE postId = ? AND deletedAt IS NULL";
		return jdbcTemplate.queryForObject(sql, postRowMapper(), postId);
	}

	@Override
	public List<Post> findPage(int offset, int size) {
		String sql = """
			SELECT * FROM posts
			WHERE deletedAt IS NULL
			ORDER BY createdAt DESC
			LIMIT ? OFFSET ? 
			""";

		return jdbcTemplate.query(sql, postRowMapper(),size, offset);
	}

	@Override
	public int countAll() {
		String sql = " SELECT COUNT(*) FROM posts WHERE deletedAt IS NULL";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public boolean update(Post post) {
		String sql = """
			UPDATE posts
			SET title=?, content=?, modifiedAt=?
			WHERE postId=? AND deletedAt IS NULL
			""";

		return jdbcTemplate.update(sql,post.getTitle(),post.getContent(),post.getModifiedAt())==1;
	}

	@Override
	public boolean softDelete(int postId) {
		String sql = "UPDATE posts SET deletedAt = NOW() WHERE postId = ?";
		return jdbcTemplate.update(sql,postId)==1;
	}

	private RowMapper<Post> postRowMapper() {
		return (rs, rowNum) -> mapRow(rs);
	}

	private Post mapRow(ResultSet rs) throws SQLException {
		return new Post(
			rs.getInt("postId"),
			rs.getInt("userId"),
			rs.getString("title"),
			rs.getString("content"),
			rs.getTimestamp("createdAt").toLocalDateTime(),
			rs.getTimestamp("modifiedAt").toLocalDateTime(),
			rs.getTimestamp("deletedAt") != null ? rs.getTimestamp("deletedAt").toLocalDateTime() : null
		);
	}

}

package com.blog.domain.post.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("posts")
			.usingGeneratedKeyColumns("postId");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", post.getUserId());
		params.put("title", post.getTitle());
		params.put("content", post.getContent());
		params.put("createdAt", post.getCreatedAt());
		params.put("modifiedAt", post.getModifiedAt());
		Number key = insert.executeAndReturnKey(params);
		if (key != null) {
			assignPostId(post, key);
			return true;
		}
		return false;
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

		return jdbcTemplate.update(sql,post.getTitle(),post.getContent(),post.getModifiedAt(),post.getPostId())==1;
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

	private void assignPostId(Post post, Number key) {
		try {
			java.lang.reflect.Field field = Post.class.getDeclaredField("postId");
			field.setAccessible(true);
			field.set(post, key.intValue());
		} catch (Exception e) {
			throw new RuntimeException("게시글 ID 할당 실패", e);
		}
	}


}

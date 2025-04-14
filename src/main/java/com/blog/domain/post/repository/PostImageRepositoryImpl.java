package com.blog.domain.post.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.blog.domain.post.domain.PostImage;

@Repository
public class PostImageRepositoryImpl implements PostImageRepository {

	private final JdbcTemplate jdbcTemplate;

	public PostImageRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean insert(PostImage image) {
		String sql = """
			INSERT INTO post_images (postId, imageUrl, createdAt)
			VALUES (?, ?, ?)
			""";

		int result = jdbcTemplate.update(sql, image.getPostId(), image.getImageUrl(), image.getCreatedAt() != null ? image.getCreatedAt() : LocalDateTime.now()
		);
		return result == 1;
	}

	@Override
	public List<PostImage> findByPostId(int postId) {
		String sql = "SELECT * FROM post_images WHERE postId = ?";
		return jdbcTemplate.query(sql, postImageRowMapper(), postId);
	}

	private RowMapper<PostImage> postImageRowMapper() {
		return (rs, rowNum) -> mapRow(rs);
	}

	private PostImage mapRow(ResultSet rs) throws SQLException {
		return new PostImage(
			rs.getInt("imageId"),
			rs.getInt("postId"),
			rs.getString("imageUrl"),
			rs.getTimestamp("createdAt").toLocalDateTime()
		);
	}

}

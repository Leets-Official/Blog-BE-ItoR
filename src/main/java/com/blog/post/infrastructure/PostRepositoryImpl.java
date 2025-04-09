package com.blog.post.infrastructure;

import com.blog.post.domain.Post;
import com.blog.post.domain.PostContent;
import com.blog.post.domain.PostContentType;
import com.blog.post.domain.PostRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbc;

    public PostRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO posts (user_id, title, created_at, updated_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getTitle());
            ps.setTimestamp(3, post.getCreatedAt());
            ps.setTimestamp(4, post.getUpdatedAt());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) post.setPostId(key.intValue());
    }

    @Override
    public void saveContents(List<PostContent> contents) {
        String sql = "INSERT INTO contents (post_id, user_id, content_type, content, `order`) VALUES (?, ?, ?, ?, ?)";

        for (PostContent content : contents) {
            jdbc.update(sql,
                    content.getPostId(),
                    content.getUserId(),
                    content.getContentType(),
                    content.getContent(),
                    content.getOrder()
            );
        }
    }

    @Override
    public Post findById(Integer postId) {
        String sql = "SELECT * FROM posts WHERE post_id = ?";
        Post post = jdbc.queryForObject(sql, postRowMapper(), postId);

        String contentSql = "SELECT * FROM contents WHERE post_id = ? ORDER BY `order` ASC";
        List<PostContent> contents = jdbc.query(contentSql, contentRowMapper(), postId);

        post.setContents(contents);
        return post;
    }

    @Override
    public void delete(Integer postId, Integer userId) {
        String sql = "DELETE FROM posts WHERE post_id = ? AND user_id = ?";
        jdbc.update(sql, postId, userId);
    }

    @Override
    public void deleteContents(Integer postId, Integer userId) {
        String sql = "DELETE FROM contents WHERE post_id = ? AND user_id = ?";
        jdbc.update(sql, postId, userId);
    }

    private RowMapper<Post> postRowMapper() {
        return (rs, rowNum) -> new Post(
                rs.getInt("post_id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                null
        );
    }

    private RowMapper<PostContent> contentRowMapper() {
        return (rs, rowNum) -> new PostContent(
                rs.getInt("content_id"),
                rs.getInt("post_id"),
                rs.getInt("user_id"),
                PostContentType.from(rs.getString("content_type")),
                rs.getString("content"),
                rs.getInt("order")
        );
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM posts ORDER BY created_at DESC";

        List<Post> posts = jdbc.query(sql, postRowMapper());

        for (Post post : posts) {
            String contentSql = "SELECT * FROM contents WHERE post_id = ? ORDER BY `order` ASC";
            List<PostContent> contents = jdbc.query(contentSql, contentRowMapper(), post.getPostId());
            post.setContents(contents);
        }

        return posts;
    }

}

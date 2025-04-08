package com.blog.domain.posts.domain.repository;

import com.blog.domain.posts.api.dto.response.PostListResponse;
import com.blog.domain.posts.api.dto.response.PostResponse;
import com.blog.domain.posts.api.dto.response.PostSummary;
import com.blog.domain.posts.domain.Posts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostsRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostsRepository(JdbcTemplate jdbcTemplate){

        this.jdbcTemplate = jdbcTemplate;
    }

    public int addPost(Posts post) {
        String sql = "INSERT INTO posts (user_id, subject) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getSubject());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public List<PostSummary> getPostsList(){
        String sql = " SELECT p.id AS post_id, p.subject, u.nickname, p.created_at FROM posts p " +
                "JOIN users u ON p.user_id = u.id ";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new PostSummary(
                        rs.getInt("post_id"),
                        rs.getString("nickname"),
                        rs.getString("subject"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                )
        );
    }

    public Posts getPostByPostId(int postId){
        String sql = "SELECT * FROM posts WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Posts.fromResultSet(rs), postId);
    }

    public int getPostUserId(int postId){
        String sql = "SELECT user_id FROM posts WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, int.class, postId);
    }

    public void updatePost(int postId, String subject){
        String sql = "UPDATE posts SET subject = ? WHERE id = ?";

        jdbcTemplate.update(sql, subject, postId);
    }
}

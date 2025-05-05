package com.blog.domain.posts.domain.repository;


import com.blog.domain.posts.api.dto.response.PostBlockResponse;
import com.blog.domain.posts.api.dto.response.PostSummary;
import com.blog.domain.posts.domain.Posts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<PostSummary> getPostsList(int offset){
        String sql = " SELECT p.id AS post_id, p.subject, u.nickname, p.created_at FROM posts p " +
                "JOIN users u ON p.user_id = u.id ORDER BY p.id DESC LIMIT 5 OFFSET ?";
        List<PostSummary> posts = jdbcTemplate.query(sql, new Object[]{offset}, (rs, rowNum) ->
                new PostSummary(
                        rs.getInt("post_id"),
                        rs.getString("nickname"),
                        rs.getString("subject"),
                        new ArrayList<>(),
                        rs.getTimestamp("created_at").toLocalDateTime()
                )
        );

        if (posts.isEmpty()) return posts;

        List<Integer> ids = posts.stream().map(PostSummary::postId).toList();
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));

        String blockSql = "SELECT post_id, content, image_url FROM post_blocks WHERE post_id IN (" + placeholders + ")";
        Map<Integer, List<PostBlockResponse>> blockMap = jdbcTemplate.query(blockSql, ids.toArray(), (rs, i) ->
                Map.entry(
                        rs.getInt("post_id"),
                        new PostBlockResponse(
                                rs.getString("content"),
                                rs.getString("image_url")
                        )
                )
        ).stream().collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.mapping(Map.Entry::getValue, Collectors.toList())
        ));

        return posts.stream()
                .map(p -> new PostSummary(
                        p.postId(),
                        p.nickname(),
                        p.subject(),
                        blockMap.getOrDefault(p.postId(), List.of()),
                        p.createdAt()
                ))
                .toList();
    }

    public Posts getPostByPostId(int postId){
        String sql = "SELECT * FROM posts WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Posts.of(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("subject"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        ), postId);
    }

    public int getPostUserId(int postId){
        String sql = "SELECT user_id FROM posts WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, int.class, postId);
    }

    public void updatePost(int postId, String subject){
        String sql = "UPDATE posts SET subject = ? WHERE id = ?";

        jdbcTemplate.update(sql, subject, postId);
    }

    public void deletePost(int postId){
        String sql = "DELETE FROM posts WHERE id = ?";

        jdbcTemplate.update(sql, postId);
    }
}

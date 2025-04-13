package com.blog.domain.comments.domain.repository;

import com.blog.domain.comments.api.dto.request.CommentsRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommentsRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentsRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addComments(int userId, CommentsRequest request){
        String sql = "INSERT INTO comments (post_id, user_id, content) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, request.postId(), userId, request.content());
    }

}

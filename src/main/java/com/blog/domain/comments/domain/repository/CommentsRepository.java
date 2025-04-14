package com.blog.domain.comments.domain.repository;

import com.blog.domain.comments.api.dto.request.CommentsRequest;
import com.blog.domain.comments.api.dto.response.CommentsResponse;
import com.blog.domain.comments.domain.Comments;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<CommentsResponse> getCommentsListByPostId(int postId){
        String sql = "SELECT c.*, u.nickname, u.profile_image FROM comments c " +
                "JOIN users u ON c.user_id = u.id WHERE c.post_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Comments comment = Comments.of(
                rs.getInt("id"),
                rs.getInt("post_id"),
                rs.getInt("user_id"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );

        return new CommentsResponse(
                rs.getString("nickname"),
                rs.getString("profile_image"),
                comment
        );
        }, postId);
    }
}

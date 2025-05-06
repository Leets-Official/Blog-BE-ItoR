package com.blog.comment.domain;

import java.sql.Timestamp;
import java.util.List;

public interface CommentRepository {

    void save(Comment comment);

    List<Comment> findByPostId(int postId);

    Comment findById(int postId, int commentId);

    void update(int commentId, int postId, int userId, String content, Timestamp updatedAt);

    void delete(int postId, int commentId, int userId);
}

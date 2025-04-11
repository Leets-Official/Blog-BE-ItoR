package com.blog.workspace.adapter.out.jdbc.comment;

import com.blog.workspace.domain.comment.Comment;

import java.time.LocalDateTime;

public class CommentJdbc {

    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CommentJdbc(Long id, Long postId, Long userId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /// from
    public static CommentJdbc from(Comment comment) {
        return new CommentJdbc(
                null,
                comment.getPostId(),
                comment.getUserId(),
                comment.getContent(),
                comment.getCreated(),
                comment.getUpdated()
        );
    }

    /// toDomain
    public Comment toDomain() {
        return Comment.fromDB(id, postId, userId, content, createdAt, updatedAt);
    }

    /// DB용 및 수정용
    public static CommentJdbc fromDB(Long id, Long postId, Long userId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CommentJdbc(id, postId, userId, content, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

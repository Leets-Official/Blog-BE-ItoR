package com.blog.domain.comment.domain;

import com.blog.domain.comment.controller.dto.request.CommentRequest;
import com.blog.global.common.BaseDomain;

import java.time.LocalDateTime;

public class Comment extends BaseDomain {
    private Long id;
    private long userId;
    private long postId;
    private String content;

    public Comment(Long id, Long userId, Long postId, String content) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }

    public Comment(Long userId, Long postId, String content) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }

    public Comment(Long id, Long userId, Long postId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }

    public static Comment of(Long userId, CommentRequest request) {
        return new Comment(userId, request.postId(), request.content());
    }

    //Getter
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPostId() {
        return postId;
    }

    public String getContent() { return content; }

    // 도메인 메서드
    public void updateContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return super.getUpdatedAt();
    }

}

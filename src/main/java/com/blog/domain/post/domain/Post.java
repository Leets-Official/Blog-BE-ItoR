package com.blog.domain.post.domain;

import com.blog.domain.post.controller.request.PostRequest;
import com.blog.global.common.BaseDomain;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class Post extends BaseDomain {

    private Long id;
    private long userId;
    private String title;
    private int commentCount = 0;

    // 생성자
    public Post(Long id, Long userId, String title, int commentCount,LocalDateTime createdAt) {
        super(createdAt);
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    public Post(Long userId, String title) {
        super();
        this.userId = userId;
        this.title = title;
    }

    public Post(Long id, Long userId, String title,int commentCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.commentCount = commentCount;
    }

    public static Post of(Long userId, PostRequest postRequest) {
        return new Post(userId, postRequest.title());
    }

    // Getter
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public int getCommentCount() {
        return commentCount;
    }

    // 도메인 메서드
    public void updateTitle(@NotBlank String title) {
        this.title = title;
    }

    public void updateUpdatedAt(@NotBlank LocalDateTime now) {
        this.updatedAt = now;
    }

}


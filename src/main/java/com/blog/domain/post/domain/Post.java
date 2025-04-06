package com.blog.domain.post.domain;

import com.blog.domain.post.controller.dto.request.PostRequest;
import com.blog.global.common.BaseDomain;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class Post extends BaseDomain {

    private Long id;
    private Long userId;
    private String title;
    private int commentCount = 0;

    // 생성자
    public Post(Long id, Long userId, String title, LocalDate createdAt) {
        super(createdAt);
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.createdAt = createdAt;
    }

    public Post(Long userId, String title) {
        super();
        this.userId = userId;
        this.title = title;
    }

    public Post(Long id, Long userId, String title, LocalDate createdAt, LocalDate updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.userId = userId;
        this.title = title;
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
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비어있을 수 없습니다.");
        }
        this.title = title;
    }

    public void updateUpdatedAt(LocalDate now) {
        if (now == null) {
            throw new IllegalArgumentException("업데이트 시간은 비어있을 수 없습니다.");
        }
        this.updatedAt = now;
    }

    // commentCount +1
    public void incrementCommentCount() {
        this.commentCount++;
    }


}

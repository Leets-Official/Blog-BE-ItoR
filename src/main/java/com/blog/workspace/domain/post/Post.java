package com.blog.workspace.domain.post;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class Post extends BaseDomain {

    private Long id;
    private final Long userId;
    private final String title;


    /// 생성자
    public Post(Long id, Long userId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.userId = userId;
        this.title = title;
    }

    /// 정적 팩토리 메서드 -> 서비스 계층 생성
    public static Post of(Long userId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Post(null, userId, title, createdAt, updatedAt);
    }

    public static Post fromDB(Long id, Long userId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Post(id, userId, title, createdAt, updatedAt);
    }

    /// @Getter
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    /// 비즈니스 로직


}

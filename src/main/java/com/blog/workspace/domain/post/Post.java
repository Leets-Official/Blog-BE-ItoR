package com.blog.workspace.domain.post;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class Post extends BaseDomain {

    private Long id;
    private final Long userId;
    private final String title;
    private final String content;

    /// 서비스 내부 생성자
    public Post(Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    /// JDBC에서 Domain으로 변환할 때 생성자
    public Post(Long id, Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    /// 비즈니스 로직


}

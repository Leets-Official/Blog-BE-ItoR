package com.blog.workspace.domain.post;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class Post extends BaseDomain {

    private Long id;
    private final Long userId;
    private final String title;
    private final String content;

    /// 생성자
    public Post(Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
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


    /// 비즈니스 로직

    public String getContent() {
        return content;
    }
}

package com.blog.workspace.adapter.out.jdbc.post;

import com.blog.workspace.domain.post.Post;

import java.time.LocalDateTime;

public class PostJdbc {

    private Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // 생성자, Id는 MYSQL 내부에서 자동으로 증가되기에 제거
    public PostJdbc(Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /// from
    public static PostJdbc from(Post post) {
        return new PostJdbc(
                post.getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreated(),
                post.getUpdated()
        );
    }

    /// toDomain
    public Post toDomain(){
        return new Post(
                this.userId,
                this.title,
                this.content,
                this.createdAt,
                this.updatedAt
        );
    }



}

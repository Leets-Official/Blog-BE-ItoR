package com.blog.workspace.adapter.out.jdbc.post;

import com.blog.workspace.domain.post.Post;

import java.time.LocalDateTime;

public class PostJdbc {

    private Long id;
    private final Long userId;
    private final String title;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // 생성자
    public PostJdbc(Long id,Long userId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /// from
    public static PostJdbc from(Post post) {
        return new PostJdbc(
                null,
                post.getUserId(),
                post.getTitle(),
                post.getCreated(),
                post.getUpdated()
        );
    }

    /// toDomain
    public Post toDomain(){
        return Post.fromDB(id, userId, title, createdAt, updatedAt);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /// Repo에서 id 넣어주는 함수
    public void setId(Long id) {
        this.id = id;
    }
}

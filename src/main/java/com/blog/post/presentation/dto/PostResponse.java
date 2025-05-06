package com.blog.post.presentation.dto;

import java.util.List;

public class PostResponse {
    private int postId;
    private int userId;
    private String title;
    private String createdAt;
    private List<PostContentDto> contents;

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<PostContentDto> getContents() { return contents; }
    public void setContents(List<PostContentDto> contents) {
        this.contents = contents;
    }
}

package com.blog.post.presentation.dto;

import java.util.List;

public class PostResponse {
    private Integer postId;
    private Integer userId;
    private String title;
    private String createdAt;
    private List<PostContentDto> contents;

    public Integer getPostId() { return postId; }
    public void setPostId(Integer postId) { this.postId = postId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<PostContentDto> getContents() { return contents; }
    public void setContents(List<PostContentDto> contents) {
        this.contents = contents;
    }
}

package com.blog.post.domain;

import java.sql.Timestamp;
import java.util.List;

public class Post {

    private int postId;
    private int userId;
    private String title;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<PostContent> contents;

    public Post(
            int postId,
            int userId,
            String title,
            Timestamp createdAt,
            Timestamp updatedAt,
            List<PostContent> contents
    ) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.contents = contents;
    }

    public int getPostId() { return postId; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public List<PostContent> getContents() { return contents; }

    public void setPostId(int postId) { this.postId = postId; }
    public void setTitle(String title) { this.title = title; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public void setContents(List<PostContent> contents) {
        this.contents = contents;
    }
}

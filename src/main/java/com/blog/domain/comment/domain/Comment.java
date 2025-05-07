package com.blog.domain.comment.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comment {
  private UUID id;
  private String content;
  private UUID authorId;
  private UUID postId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Comment(UUID id, String content, UUID authorId, UUID postId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.content = content;
    this.authorId = authorId;
    this.postId = postId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public UUID getAuthorId() {
    return authorId;
  }

  public UUID getPostId() {
    return postId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void updateContent(String content) {
    this.content = content;
    this.updatedAt = LocalDateTime.now();
  }
}

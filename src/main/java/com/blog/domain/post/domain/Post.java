package com.blog.domain.post.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Post {
  private UUID id;
  private String title;
  private String content;
  private UUID authorId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Post(UUID id, String title, String content, UUID authorId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.authorId = authorId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public UUID getAuthorId() {
    return authorId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void update(String title, String content) {
    this.title = title;
    this.content = content;
    this.updatedAt = LocalDateTime.now();
  }
}

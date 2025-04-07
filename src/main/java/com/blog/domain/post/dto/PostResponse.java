package com.blog.domain.post.dto;

import com.blog.domain.post.domain.Post;
import java.time.LocalDateTime;
import java.util.UUID;

public class PostResponse {
  private UUID id;
  private String title;
  private String content;
  private UUID authorId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public PostResponse(UUID id, String title, String content, UUID authorId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.authorId = authorId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static PostResponse from(Post post) {
    return new PostResponse(
        post.getId(),
        post.getTitle(),
        post.getContent(),
        post.getAuthorId(),
        post.getCreatedAt(),
        post.getUpdatedAt()
    );
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
}

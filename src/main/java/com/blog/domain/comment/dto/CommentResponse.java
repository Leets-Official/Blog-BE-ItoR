package com.blog.domain.comment.dto;

import com.blog.domain.comment.domain.Comment;
import java.time.LocalDateTime;
import java.util.UUID;

public class CommentResponse {
  private UUID id;
  private String content;
  private UUID authorId;
  private UUID postId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public CommentResponse(UUID id, String content, UUID authorId, UUID postId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.content = content;
    this.authorId = authorId;
    this.postId = postId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static CommentResponse from(Comment comment) {
    return new CommentResponse(
        comment.getId(),
        comment.getContent(),
        comment.getAuthorId(),
        comment.getPostId(),
        comment.getCreatedAt(),
        comment.getUpdatedAt()
    );
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
}

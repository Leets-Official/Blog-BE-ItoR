package com.blog.domain.comment.dto;

import java.util.UUID;

public class CommentRequest {
  private String content;
  private UUID postId;
  private UUID authorId;

  public String getContent() {
    return content;
  }

  public UUID getPostId() {
    return postId;
  }

  public UUID getAuthorId() {
    return authorId;
  }
}

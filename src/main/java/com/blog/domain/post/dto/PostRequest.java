package com.blog.domain.post.dto;

import java.util.UUID;

public class PostRequest {
  private String title;
  private String content;
  private UUID authorId;

  public UUID getAuthorId() {
    return authorId;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }
}

package com.blog.domain.image.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Image {
  private UUID id;
  private String originalName;
  private String storedName;
  private String url;
  private LocalDateTime createdAt;

  public Image(UUID id, String originalName, String storedName, String url, LocalDateTime createdAt) {
    this.id = id;
    this.originalName = originalName;
    this.storedName = storedName;
    this.url = url;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public String getOriginalName() {
    return originalName;
  }

  public String getStoredName() {
    return storedName;
  }

  public String getUrl() {
    return url;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}

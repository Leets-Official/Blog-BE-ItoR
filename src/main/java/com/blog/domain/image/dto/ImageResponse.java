package com.blog.domain.image.dto;

import com.blog.domain.image.domain.Image;

import java.util.UUID;

public class ImageResponse {
  private UUID id;
  private String url;

  public ImageResponse(UUID id, String url) {
    this.id = id;
    this.url = url;
  }

  public static ImageResponse from(Image image) {
    return new ImageResponse(image.getId(), image.getUrl());
  }

  public UUID getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }
}

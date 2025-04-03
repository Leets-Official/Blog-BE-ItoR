package com.blog.domain.user.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
  private UUID id;
  private String nickname;
  private String password;
  private String email;
  private String profileImageUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public User(UUID id, String nickname, String password, String email, String profileImageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.nickname = nickname;
    this.password = password;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public String getNickname() {
    return nickname;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}

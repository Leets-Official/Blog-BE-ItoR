package com.blog.domain.user.dto;

import com.blog.domain.user.domain.User;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserProfileResponse {
  private UUID id;
  private String nickname;
  private String email;
  private String profileImageUrl;
  private LocalDateTime createdAt;

  public UserProfileResponse(UUID id, String nickname, String email, String profileImageUrl, LocalDateTime createdAt) {
    this.id = id;
    this.nickname = nickname;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
    this.createdAt = createdAt;
  }

  public static UserProfileResponse from(User user) {
    return new UserProfileResponse(
        user.getId(),
        user.getNickname(),
        user.getEmail(),
        user.getProfileImageUrl(),
        user.getCreatedAt()
    );
  }

  public UUID getId() {
    return id;
  }

  public String getNickname() {
    return nickname;
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
}

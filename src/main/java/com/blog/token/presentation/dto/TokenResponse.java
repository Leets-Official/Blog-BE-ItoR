package com.blog.token.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.blog.user.presentation.dto.UserResponse;

public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_at")
    private String expiresAt;
    private UserResponse user;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getExpiresAt() { return expiresAt; }
    public UserResponse getUser() { return user; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
    public void setUser(UserResponse user) { this.user = user; }
}

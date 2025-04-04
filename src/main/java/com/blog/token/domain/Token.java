package com.blog.token.domain;


import java.sql.Timestamp;

public class Token {
    private Long id;
    private final Long userId;
    private String refreshToken;
    private Timestamp expiresAt;
    private final Timestamp createdAt;

    public Token(Long userId, String refreshToken, Timestamp expiresAt, Timestamp createdAt) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getRefreshToken() { return refreshToken; }
    public Timestamp getExpiresAt() { return expiresAt; }
    public Timestamp getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void updateToken(String refreshToken, Timestamp expiresAt) {
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }
}
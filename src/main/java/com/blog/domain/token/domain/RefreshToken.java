package com.blog.domain.token.domain;

import java.time.LocalDateTime;

public class RefreshToken {

	private final int userId;
	private final String refreshToken;
	private final LocalDateTime expiredAt;
	private final LocalDateTime createdAt;

	private RefreshToken(int userId, String refreshToken, LocalDateTime expiredAt, LocalDateTime createdAt) {
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.expiredAt = expiredAt;
		this.createdAt = createdAt;
	}

	public static RefreshToken of(int userId, String token,
		LocalDateTime expiredAt, LocalDateTime createdAt) {
		return new RefreshToken(userId, token, expiredAt, createdAt);
	}

	public static RefreshToken create(int userId, String token, LocalDateTime expiredAt) {
		return new RefreshToken(userId, token, expiredAt, LocalDateTime.now());
	}

	public int getUserId() {
		return userId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}

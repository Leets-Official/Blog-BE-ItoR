package com.blog.domain.auth.login.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoLoginDTO {
	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private int expiresIn;

	@JsonProperty("scope")
	private String scope;

	@JsonProperty("refresh_token_expires_in")
	private int refreshTokenExpiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public String getScope() {
		return scope;
	}

	public int getRefreshTokenExpiresIn() {
		return refreshTokenExpiresIn;
	}
}

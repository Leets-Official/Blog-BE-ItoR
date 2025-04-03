package com.blog.global.auth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTokenResponseDto {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("refresh_token")
	private String refreshToken;

	public KakaoTokenResponseDto( String accessToken, String tokenType, String refreshToken) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.refreshToken = refreshToken;
	}

	public KakaoTokenResponseDto() {
	}


	public String getAccessToken() {
		return accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}


package com.blog.domain.token.dto;

public class TokenReissueRequestDto {

	private String refreshToken;

	public TokenReissueRequestDto() {
	}

	public TokenReissueRequestDto(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}

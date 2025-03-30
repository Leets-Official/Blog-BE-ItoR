package com.blog.global.auth.dto;

public class LoginResponseDto {

	private final String accessToken;

	public LoginResponseDto(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}
}

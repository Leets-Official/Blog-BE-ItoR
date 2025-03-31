package com.blog.global.auth.dto;

public class SignUpResponseDto {

	private final String message;

	public SignUpResponseDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

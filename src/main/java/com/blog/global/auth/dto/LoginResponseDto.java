package com.blog.global.auth.dto;

public class LoginResponseDto {

	private final String email;
	private String password;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public LoginResponseDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}

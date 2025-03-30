package com.blog.global.auth.dto;

public class LoginRequestDto {

	private final String email;
	private String password;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public LoginRequestDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}

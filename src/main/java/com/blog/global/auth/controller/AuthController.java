package com.blog.global.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.global.auth.dto.LoginRequestDto;
import com.blog.global.auth.dto.LoginResponseDto;
import com.blog.global.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) throws Exception {
		String token = authService.login(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(new LoginResponseDto(token));
	}

}

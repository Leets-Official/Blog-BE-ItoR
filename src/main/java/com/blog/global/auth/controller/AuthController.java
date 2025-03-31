package com.blog.global.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.global.auth.dto.LoginRequestDto;
import com.blog.global.auth.dto.LoginResponseDto;
import com.blog.global.auth.dto.SignUpRequestDto;
import com.blog.global.auth.dto.SignUpResponseDto;
import com.blog.global.auth.service.AuthService;
import com.blog.global.auth.service.KakaoAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final KakaoAuthService kakaoAuthService;

	public AuthController(AuthService authService, KakaoAuthService kakaoAuthService) {
		this.authService = authService;
		this.kakaoAuthService = kakaoAuthService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) throws Exception {
		String token = authService.login(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(new LoginResponseDto(token));
	}

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signup(@RequestBody @Valid SignUpRequestDto request) {
		SignUpResponseDto response = authService.signUp(request);
		return ResponseEntity.ok(response);
	}

	// 카카오에서 발급한 인가 코드를 파라미터로 받아서 JWT를 발급함
	@GetMapping("/kakao/callback")
	public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam String code) throws Exception {
		// 사용자 정보를 확인하여, 회원가입 또는 기존 사용자 조회 후 JWT를 발급
		String token = kakaoAuthService.kakaoLogin(code);
		// 발급된 JWT를 LoginResponseDto에 담아 응답으로 반환
		return ResponseEntity.ok(new LoginResponseDto(token));
	}


}

package com.blog.global.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.domain.token.dto.TokenReissueRequestDto;
import com.blog.global.auth.dto.LoginRequestDto;
import com.blog.global.auth.dto.LoginResponseDto;
import com.blog.global.auth.dto.SignUpRequestDto;
import com.blog.global.auth.dto.SignUpResponseDto;
import com.blog.global.auth.service.AuthService;
import com.blog.global.auth.service.KakaoAuthService;
import com.blog.global.common.dto.GlobalResponseDto;

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
	public ResponseEntity<GlobalResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto request) throws Exception {
		LoginResponseDto response = authService.login(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(GlobalResponseDto.success(response));
	}

	@PostMapping("/signup")
	public ResponseEntity<GlobalResponseDto<SignUpResponseDto>> signup(@RequestBody @Valid SignUpRequestDto request) {
		SignUpResponseDto response = authService.signUp(request);
		return ResponseEntity.ok(GlobalResponseDto.success(response));
	}

	// 카카오에서 발급한 인가 코드를 파라미터로 받아서 JWT를 발급함
	@GetMapping("/kakao/callback")
	public ResponseEntity<GlobalResponseDto<LoginResponseDto>> kakaoLogin(@RequestParam String code) throws Exception {
		LoginResponseDto response = kakaoAuthService.kakaoLogin(code);
		return ResponseEntity.ok(GlobalResponseDto.success(response));
	}

	@PostMapping("/reissue")
	public ResponseEntity<GlobalResponseDto<LoginResponseDto>> reissue(@RequestBody TokenReissueRequestDto request) throws Exception {
		LoginResponseDto response = authService.reissueAccessToken(request.getRefreshToken());
		return ResponseEntity.ok(GlobalResponseDto.success(response));
	}

}

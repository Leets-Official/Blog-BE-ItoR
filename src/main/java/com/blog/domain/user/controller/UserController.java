package com.blog.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.dto.UserInfoResponseDto;
import com.blog.domain.user.dto.UserProfileUpdateRequestDto;
import com.blog.domain.user.dto.UserProfileUpdateResponseDto;
import com.blog.domain.user.service.UserService;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.common.dto.GlobalResponseDto;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final JwtUtil jwtUtil;

	public UserController(UserService userService, JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@PatchMapping("/profile")
	public ResponseEntity<GlobalResponseDto<UserProfileUpdateResponseDto>> updateProfile(
		@RequestHeader("Authorization") String bearerToken,
		@RequestBody UserProfileUpdateRequestDto requestDto
	) {
		int userId = Integer.parseInt(jwtUtil.parseUserIdFromBearerToken(bearerToken));

		UserProfileUpdateResponseDto response = userService.updateProfile(
			userId,
			requestDto.password(),
			requestDto.nickName(),
			requestDto.profileImageUrl()
		);

		return ResponseEntity.ok(GlobalResponseDto.success(response));
	}

	@GetMapping("/{userId}/profile")
	public ResponseEntity<GlobalResponseDto<UserInfoResponseDto>> getUserProfile(@PathVariable int userId) {
		UserInfoResponseDto response = userService.getUserProfile(userId);
		return ResponseEntity.ok(GlobalResponseDto.success(response));
	}


}

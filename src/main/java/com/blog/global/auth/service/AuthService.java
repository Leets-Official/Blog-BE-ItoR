package com.blog.global.auth.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.auth.dto.SignUpRequestDto;
import com.blog.global.auth.dto.SignUpResponseDto;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.config.error.ErrorCode;
import com.blog.global.config.error.exception.CommonException;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	public String login(String email, String password) throws Exception {

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));

		//비밀번호 일치 검사 (평문 비교, 추후 해시로 개선 가능)
		if (!user.getPassword().equals(password)) {
			throw new CommonException(ErrorCode.INVALID_PASSWORD);
		}

		// 토큰 생성 후 반환
		return jwtUtil.createToken(String.valueOf(user.getUserId()));
	}

	public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

		if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
			throw new CommonException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		if (!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordConfirm())) {
			throw new CommonException(ErrorCode.PASSWORD_MISMATCH);
		}

		User user = User.createEmailUser(
			signUpRequestDto.getName(),
			signUpRequestDto.getNickName(),
			signUpRequestDto.getEmail(),
			signUpRequestDto.getPassword(),
			signUpRequestDto.getBirthDate(),
			signUpRequestDto.getIntroduce(),
			signUpRequestDto.getProfileImageUrl()
		);

		userRepository.save(user);

		return new SignUpResponseDto("회원가입 성공!");
	}

	}
}

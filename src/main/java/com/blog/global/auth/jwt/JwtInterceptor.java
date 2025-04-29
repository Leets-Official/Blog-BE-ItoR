package com.blog.global.auth.jwt;

import org.springframework.web.servlet.HandlerInterceptor;

import com.blog.global.config.error.ErrorCode;
import com.blog.global.config.error.exception.CommonException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

	private final JwtUtil jwtUtil;

	public JwtInterceptor(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String authHeader = request.getHeader("Authorization");

		if( authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new CommonException(ErrorCode.INVALID_TOKEN);
		}

		String token = authHeader.substring(7);

		if (!jwtUtil.validateToken(token)) {
			throw new CommonException(ErrorCode.INVALID_TOKEN);
		}

		String userId = jwtUtil.extractUserId(token);
		request.setAttribute("userId", userId);

		return true;
	}
}

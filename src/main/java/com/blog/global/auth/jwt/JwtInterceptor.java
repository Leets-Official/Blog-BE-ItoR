package com.blog.global.auth.jwt;

import org.springframework.web.servlet.HandlerInterceptor;

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
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("올바르지 않은 Authorization Header");
			return false;
		}

		String token = authHeader.substring(7);

		if (!jwtUtil.validateToken(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("올바르지 않거나 만료된 토큰");
			return false;
		}

		String userId = jwtUtil.extractUserId(token);
		request.setAttribute("userId", userId);

		return true;
	}
}

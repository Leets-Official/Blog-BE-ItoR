package com.blog.global.interceptor;

import com.blog.global.security.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class PermissionInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public PermissionInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Gpt 도움으로 작성..

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getTokenFromHeader(request);

        if (token == null || !isValidTokenFormat(token)) {
            sendUnauthorizedResponse(response, "토큰이 없습니다.");
            return false;
        }

        try {
            String role = jwtUtil.getUserRole(token);
            String requestURI = request.getRequestURI();

            if (isAdminApi(requestURI) && !isAdmin(role)) {
                sendForbiddenResponse(response, "관리자 권한이 필요합니다.");
                return false;
            }

            return true;
        } catch (JwtException e) {
            sendUnauthorizedResponse(response, "유효하지 않은 토큰입니다.");
            return false;
        }
    }

    // 토큰 헤더에서 가져오기
    private String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 포맷이 "Bearer"로 시작하는지 확인
    private boolean isValidTokenFormat(String token) {
        return token.startsWith("Bearer ");
    }

    // 요청 URL이 관리자 API인지 확인
    private boolean isAdminApi(String requestURI) {
        return requestURI.startsWith("/admin");
    }

    // 역할이 관리자 역할인지 확인
    private boolean isAdmin(String role) {
        return "ADMIN".equals(role);
    }

    // 유효하지 않은 토큰에 대해 응답을 보냄
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }

    // 권한 부족에 대한 응답
    private void sendForbiddenResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(message);
    }
}

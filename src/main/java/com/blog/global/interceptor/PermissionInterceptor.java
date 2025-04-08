package com.blog.global.interceptor;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.global.config.AdminConfig;
import com.blog.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class PermissionInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;
    private final AdminConfig adminConfig;  // 의존성 주입

    public PermissionInterceptor(JwtUtil jwtUtil, AdminConfig adminConfig) {

        this.jwtUtil = jwtUtil;
        this.adminConfig = adminConfig;
    }

    // Gpt 도움으로 작성..

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // GET 요청이고 /posts/{postId} 형태면 통과
        if (method.equals("GET") && uri.matches("/posts/\\d+")) {
            return true;
        }

        String token = getTokenFromHeader(request);

        if (token == null || !isValidTokenFormat(token)) {

            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        token = token.substring("Bearer ".length());

        try {
            String email = jwtUtil.getEmailFromToken(token);  // 이메일을 추출해서
            String requestURI = request.getRequestURI();

            if (isAdminApi(requestURI) && !isAdmin(email)) {

                throw new CustomException(ErrorCode.Forbidden);
            }

            return true;
        } catch (CustomException e) {

            throw e;
        } catch (Exception e) {

            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private boolean isValidTokenFormat(String token) {
        return token.startsWith("Bearer ");
    }

    private boolean isAdminApi(String requestURI) {
        return requestURI.startsWith("/admin");
    }

    private boolean isAdmin(String email) {
        return adminConfig.getEmail().equalsIgnoreCase(email);
    }
}

package com.blog.common.security.jwt.handler;

import com.blog.common.security.jwt.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    // 로그
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationInterceptor.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFailureInterceptor failureHandler;

    public JwtAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider, JwtAuthenticationFailureInterceptor failureHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.failureHandler = failureHandler;
    }

    // 토큰 인증을 처리할 메서드
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorization = request.getHeader("Authorization");

        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new SecurityException("토큰이 제공되지 않았습니다.");
            }

            String token = authorization.substring(7); // "Bearer " 제거
            if (!jwtTokenProvider.validateJwt(token)) {
                throw new SecurityException("유효하지 않은 토큰입니다.");
            }

            log.info("사용자 인증 완료: {}", request.getRequestURI());
            return true;

        } catch (SecurityException e) {
            // 실패 핸들러 호출
            failureHandler.handle(request, response, e);
            return false;
        }
    }



    // 예외 발생시, 처리할 메서드
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

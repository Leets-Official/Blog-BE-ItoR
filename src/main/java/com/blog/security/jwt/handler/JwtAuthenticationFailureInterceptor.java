package com.blog.security.jwt.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JwtAuthenticationFailureInterceptor implements HandlerInterceptor {

    public void handle(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {

        // 401 응답
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\": \"" + exception.getMessage() + "\", \"status\": 401}");
    }

}



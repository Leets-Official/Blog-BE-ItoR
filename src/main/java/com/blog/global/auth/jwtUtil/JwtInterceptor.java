package com.blog.global.auth.jwtUtil;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getHeader("Authorization");
    if (token == null || !token.startsWith("Bearer ")) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
      return false;
    }
    token = token.substring(7); // "Bearer " 제거
    if (!JwtUtil.validateToken(token)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
      return false;
    }
    request.setAttribute("user", JwtUtil.getUserInfo(token));
    return true;
  }
}

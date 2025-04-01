package com.blog.global.security;


import com.blog.domain.user.repository.TokenStore;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    private CustomTokenUtil customTokenUtil = new CustomTokenUtil();

    public TokenFilter() {
        this.customTokenUtil = customTokenUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        // 검증하지 않아도 되는 것만 적기
        if (path.startsWith("/auth") || path.startsWith("/login") || path.startsWith("/join") || path.startsWith("/public")) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = getTokenFromRequest(request);

        if (accessToken != null && CustomTokenUtil.validateToken(accessToken)) {
            String userId = customTokenUtil.getUserFromToken(accessToken).toString();
            request.setAttribute("userId", userId);
            chain.doFilter(request, response);
            return;
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing refresh token");

    }


    // 헤더에서 Access Token 가져오기
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

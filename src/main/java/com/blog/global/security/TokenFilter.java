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

    // dispathcher servlet에서 tomcat으로 요청이 들어올 때마다 호출
    // 서블릿 필터 체인내에서 작동해서 http 요청이 처리되기 전에 필터체인을 통해 들어옴
    // 클라이언트가 요청을 서버로 보내면 첫번째 요청을 가로채고 검증 후 유효하면 처리
    // 헤더의 token을 추출해서 검증(이 사용자가 인증이 되었는지확인)
    // auth/login또는 /public으로 시작하면 로그인 페이지나 공개된 리소스는 인증을 건너띄도록
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        // 검증하지 않아도 되는 것만 적기|| path.startsWith("/refresh")
        if (path.startsWith("/auth") || path.startsWith("/login") || path.startsWith("/join")  || path.startsWith("/public")) {
            chain.doFilter(request, response);
            return;
        }

        // 헤더에서 토큰을 추출하여 검증
        String accessToken = getTokenFromRequest(request);
        //  검증은 CustomTokenUtil에서하기
        // 1. 토큰이 유효한지 2. 만료되지 않았는지 3. 서명이 올바른지  4. 토큰 구조가 올바른지

        if (accessToken != null && CustomTokenUtil.validateToken(accessToken)) {
            // 토큰 유효성 검증 후, 사용자 정보 추출
            String userId = customTokenUtil.getUserFromToken(accessToken).toString();
            request.setAttribute("userId", userId);

            chain.doFilter(request, response);
            return;
        }
        // Refresh Token이 없거나 유효하지 않으면 에러 처리
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing refresh token");

    }


    // 🔹 헤더에서 Access Token 가져오기
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}

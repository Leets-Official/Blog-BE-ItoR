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
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    TokenStore tokenStore = new TokenStore();

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

        // 검증하지 않아도 되는 것만 적기
        if (path.startsWith("/auth") || path.startsWith("/login") || path.startsWith("/public")) {
            chain.doFilter(request, response);
            return;
        }

        // 헤더에서 토큰을 추출하여 검증
        String accessToken = getTokenFromRequest(request);
        //  검증은 CustomTokenUtil에서하기
        // 1. 토큰이 유효한지 2. 만료되지 않았는지 3. 서명이 올바른지  4. 토큰 구조가 올바른지

        if (accessToken != null && CustomTokenUtil.validateToken(accessToken)) {
            // 토큰 유효성 검증 후, 사용자 정보 추출
            String userId = CustomTokenUtil.getUserFromToken(accessToken).toString();
            request.setAttribute("userId", userId);

            //??????
            addCookie(userId, response);

            chain.doFilter(request, response);
            return;
        }
// ✅ 2. Access Token이 만료된 경우 -> Refresh Token 확인
        String refreshToken = getRefreshTokenFromCookies(request);

        if (refreshToken != null) {
            // Refresh Token에서 사용자 정보 추출
            Map<String, Object> userInfo = CustomTokenUtil.getUserFromToken(refreshToken);

            // Refresh Token이 서버에 저장되어 있고 유효한 경우
            if (userInfo != null && tokenStore.isRefreshTokenStoredInServer((Long) userInfo.get("userId"), refreshToken)) {
                // 🔥 Refresh Token이 유효하면 새로운 Access Token 발급
                String newAccessToken = CustomTokenUtil.generateAccessToken((Long) userInfo.get("userId"), (String) userInfo.get("email"));

                // ✅ 새로운 Access Token을 헤더에 추가
                response.setHeader("Authorization", "Bearer " + newAccessToken);

                // ✅ 요청 속성에 사용자 정보 추가
                request.setAttribute("userId", userInfo.get("userId").toString());

                // 필터 체인 계속 진행
                chain.doFilter(request, response);
                return;
            }
        }

// Refresh Token이 없거나 유효하지 않으면 에러 처리
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing refresh token");


    }

    private static void addCookie(String userId, HttpServletResponse response) {
        Cookie userIdCookie = new Cookie("userId", userId);
        userIdCookie.setMaxAge(60 * 60);
        response.addCookie(userIdCookie);
    }

    // 🔹 헤더에서 Access Token 가져오기
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 🔹 쿠키에서 Refresh Token 가져오기
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst();

        return refreshTokenCookie.map(Cookie::getValue).orElse(null);
    }
}

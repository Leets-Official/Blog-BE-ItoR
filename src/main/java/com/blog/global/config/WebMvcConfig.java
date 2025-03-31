package com.blog.global.config;

import com.blog.global.interceptor.PermissionInterceptor;
import com.blog.global.security.jwt.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtUtil jwtUtil;

    public WebMvcConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PermissionInterceptor(jwtUtil))
                .addPathPatterns("/**") 
                .excludePathPatterns("/auth", "/login", "/admin"); // 회원가입, 로그인, 관리자페이지 제외
    }
}

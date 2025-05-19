package com.blog.global.config;

import com.blog.global.interceptor.PermissionInterceptor;
import com.blog.global.security.jwt.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtUtil jwtUtil;
    private final AdminConfig adminConfig;

    public WebMvcConfig(JwtUtil jwtUtil, AdminConfig adminConfig) {
        this.jwtUtil = jwtUtil;
        this.adminConfig = adminConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new PermissionInterceptor(jwtUtil, adminConfig))
                .addPathPatterns("/**")
                .excludePathPatterns("/auth, /auth/**", "/login/**", "/posts/list");
    }

}

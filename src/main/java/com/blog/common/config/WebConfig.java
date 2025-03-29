package com.blog.common.config;

import com.blog.common.security.jwt.handler.JwtAuthenticationInterceptor;
import com.blog.common.security.jwt.handler.JwtAuthenticationFailureInterceptor;
import com.blog.common.security.jwt.provider.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFailureInterceptor failureHandler;

    public WebConfig(JwtTokenProvider jwtTokenProvider, JwtAuthenticationFailureInterceptor failureHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.failureHandler = failureHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor(jwtTokenProvider, failureHandler))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/auth/**", "/login", "/logout",
                        "/css/**", "/*.ico", "/error"
                );
    }
}

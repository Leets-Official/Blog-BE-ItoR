package com.blog.common.config;

import com.blog.security.jwt.handler.JwtAuthenticationInterceptor;
import com.blog.security.jwt.handler.JwtAuthenticationFailureInterceptor;
import com.blog.security.jwt.provider.JwtTokenProvider;
import com.blog.security.resolver.RequestUserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /// JWT
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFailureInterceptor failureHandler;

    /// ArgumentResolver 추가
    private final RequestUserIdArgumentResolver userIdArgumentResolver;

    public WebConfig(JwtTokenProvider jwtTokenProvider, JwtAuthenticationFailureInterceptor failureHandler, RequestUserIdArgumentResolver userIdArgumentResolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.failureHandler = failureHandler;
        this.userIdArgumentResolver = userIdArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor(jwtTokenProvider, failureHandler))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/auth/**", "/login", "/logout",
                        "/css/**", "/*.ico", "/error",
                        "/posts/list/**"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        /// add를 통해 스프링에 추가
        resolvers.add(userIdArgumentResolver);
    }
}

package com.blog.global.config;

import com.blog.global.auth.jwtUtil.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final JwtInterceptor jwtInterceptor;

  public WebConfig(JwtInterceptor jwtInterceptor) {
    this.jwtInterceptor = jwtInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor)
        .addPathPatterns("/posts/**", "/comments/**") // 보호할 API 경로
        .excludePathPatterns("/login", "/signup", "/public/**"); // 예외 처리
  }
}

package com.blog.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.blog.global.auth.jwt.JwtInterceptor;
import com.blog.global.auth.jwt.JwtUtil;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

	private final JwtUtil jwtUtil;

	@Autowired
	public WebConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new JwtInterceptor(jwtUtil))
			.addPathPatterns("/api/**") // 인증이 필요한 경로들은 /api로 시작
			.excludePathPatterns("/auth/**", "/posts/list/**"); //로그인,회원가입은 Jwt 검사 제외

	}
}

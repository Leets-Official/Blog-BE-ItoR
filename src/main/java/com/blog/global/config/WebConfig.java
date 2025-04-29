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
			// 인증이 필요한 모든 댓글·포스트 쓰기/수정/삭제 경로
			.addPathPatterns(
				"/posts/*/comments/**",
				"/posts",         // POST /posts
				"/posts/**"       // PUT/DELETE /posts/{id}
			)
			// 인증 불필요한 경로
			.excludePathPatterns(
				"/auth/**",
				"/posts/list/**",    // GET /posts/list
				"/posts/*/comments", // GET /posts/{id}/comments
				"/posts/*/comments/" // trailing slash
			);
	}
}

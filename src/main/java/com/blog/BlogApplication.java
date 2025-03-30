package com.blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.blog.domain.user.service.UserService;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
/*  DB 유저 생성 테스트코드
	@Bean
	public CommandLineRunner runner(UserService userService) {
		return args -> {
			userService.createTestUser();
		};
	}

 */
}

package com.blog.domain.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.domain.UserType;
import com.blog.domain.user.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

/*  DB테스트 유저 생성 테스트 코드
	public void createTestUser() {
		User user = new User();
		user.setName("홍길동");
		user.setNickName("길동이");
		user.setEmail("test@example.com");
		user.setPassword("1234"); // 실제론 암호화 필요
		user.setUserType(UserType.LOCAL);
		user.setBirthDate(LocalDate.of(2001, 10, 15));
		user.setIntroduce("안녕하세요, 테스트 유저입니다!");
		user.setProfileImageUrl("https://example.com/profile.jpg");
		user.setCreatedAt(LocalDateTime.now());

		userRepository.save(user);
		System.out.println("✅ 테스트 유저 저장 완료!");
	}

 */
}

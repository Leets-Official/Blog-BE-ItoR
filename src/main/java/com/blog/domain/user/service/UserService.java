package com.blog.domain.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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

	public String findNicknameByUserId(int userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		return userOpt.map(User::getNickName)
			.orElse(null);
	}

	public String findProfileImageUrlByUserId(int userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		return userOpt.map(User::getProfileImageUrl)
			.orElse(null);
	}
}

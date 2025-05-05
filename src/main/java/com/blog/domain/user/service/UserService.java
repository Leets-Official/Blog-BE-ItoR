package com.blog.domain.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.domain.UserType;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.config.error.ErrorCode;
import com.blog.global.config.error.exception.CommonException;

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

	public User updateProfile(int userId, String newPassword, String newNickName, String newProfileImageUrl) {
		User user = getUserById(userId);
		User updatedUser = applyProfileChanges(user, newPassword, newNickName, newProfileImageUrl);
		userRepository.update(updatedUser);
		return updatedUser;
	}

	private User getUserById(int userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
	}

	private User applyProfileChanges(User user, String password, String nickName, String profileImageUrl) {
		return new User(
			user.getUserId(),
			user.getName(),
			nonNullOr(user.getNickName(), nickName),
			user.getEmail(),
			nonNullOr(user.getPassword(), password),
			user.getUserType(),
			user.getBirthDate(),
			user.getIntroduce(),
			nonNullOr(user.getProfileImageUrl(), profileImageUrl),
			user.getCreatedAt()
		);
	}

	// 바꾸고싶은 필드만 요청에 넣으면 그거만 바뀌고 나머지는 원래 기존의 값 유지
	private String nonNullOr(String original, String updated) {
		return updated != null ? updated : original;
	}

}

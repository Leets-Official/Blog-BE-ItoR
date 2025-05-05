package com.blog.domain.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.domain.UserType;
import com.blog.domain.user.dto.UserProfileUpdateResponseDto;
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

	public UserProfileUpdateResponseDto updateProfile(int userId, String newPassword, String newNickName, String newProfileImageUrl) {
		User user = getUserById(userId);

		validateUpdatableFields(newPassword, newNickName, newProfileImageUrl);
		validateChanges(user, newPassword, newNickName, newProfileImageUrl);

		User updatedUser = applyProfileChanges(user, newPassword, newNickName, newProfileImageUrl);
		userRepository.update(updatedUser);

		// 여기서 DTO로 변환해서 리턴
		return new UserProfileUpdateResponseDto(
			updatedUser.getNickName(),
			updatedUser.getProfileImageUrl()
		);
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

	private void validateUpdatableFields(String pw, String nick, String img) {
		if (pw == null && nick == null && img == null) {
			throw new CommonException(ErrorCode.NO_UPDATABLE_FIELDS);
		}
	}

	// 기존과 동일한 값인지 검증
	private void validateChanges(User user, String pw, String nick, String img) {
		boolean samePw = pw == null || pw.equals(user.getPassword());
		boolean sameNick = nick == null || nick.equals(user.getNickName());
		boolean sameImg = img == null || img.equals(user.getProfileImageUrl());

		if (samePw && sameNick && sameImg) {
			throw new CommonException(ErrorCode.NO_CHANGES_DETECTED);
		}
	}

}

package com.blog.domain.user.dto;

import com.blog.domain.user.domain.User;

public record UserProfileUpdateResponseDto(String nickname,
										   String profileImageUrl) {


	public static UserProfileUpdateResponseDto from(User user) {
		return new UserProfileUpdateResponseDto(
			user.getNickName(),
			user.getProfileImageUrl()
		);
	}
}

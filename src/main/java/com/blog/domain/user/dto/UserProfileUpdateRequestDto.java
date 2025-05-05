package com.blog.domain.user.dto;

public record UserProfileUpdateRequestDto(String nickname,
										  String passWord,
										  String profileImageUrl) {
}

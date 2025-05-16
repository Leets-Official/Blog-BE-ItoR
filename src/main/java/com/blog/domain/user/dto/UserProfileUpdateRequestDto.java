package com.blog.domain.user.dto;

public record UserProfileUpdateRequestDto(String nickName,
										  String password,
										  String profileImageUrl) {
}

package com.blog.domain.user.dto;

import java.util.List;

import com.blog.domain.post.dto.PostSummaryDto;

public record UserInfoResponseDto(String profileImageUrl,
								  String nickName,
								  String introduce,
								  List<PostSummaryDto> posts) {
}

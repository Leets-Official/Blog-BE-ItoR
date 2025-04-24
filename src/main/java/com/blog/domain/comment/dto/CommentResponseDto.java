package com.blog.domain.comment.dto;

import java.time.LocalDate;

public record CommentResponseDto(
	int commentId,
	String nickname,
	String profileImageUrl,
	String content,
	LocalDate createdDate
) {
}

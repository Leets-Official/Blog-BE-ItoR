package com.blog.domain.comment.controller.dto.response;

import com.blog.domain.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,

        String nickname,
        String profileImage,

        LocalDateTime timestamp, // 생성일자 또는 수정일자
        boolean isUpdated, // 수정 여부

        String comment
) {
    public static CommentResponse from(Comment comment, String nickname, String profileImage) {
        // 수정 여부가 true일 경우 업데이트 시간을, 아니면 생성 시간을 사용
        LocalDateTime timestamp = comment.getUpdatedAt() != null ? comment.getUpdatedAt() : comment.getCreatedAt();
        return new CommentResponse(
                comment.getId(),
                nickname,
                profileImage,
                timestamp,
                comment.getUpdatedAt() != null,
                comment.getContent()
        );
    }
}


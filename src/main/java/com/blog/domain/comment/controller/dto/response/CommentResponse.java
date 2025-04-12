package com.blog.domain.comment.controller.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,

        String nickname,
        String profileImage,

        LocalDateTime createdAt,
        boolean isUpdated,

        String comment
) {}

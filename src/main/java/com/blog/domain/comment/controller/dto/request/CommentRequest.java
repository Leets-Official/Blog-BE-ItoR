package com.blog.domain.comment.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull
        long postId,

        @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
        String content
) {}


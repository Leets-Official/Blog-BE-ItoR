package com.blog.domain.comment.controller.request;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdatedRequest(
        @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
        String content)
{}

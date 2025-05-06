package com.blog.workspace.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull(message = "게시글 Id는 비어 있을 수 없습니다.")
        Long postId,

        @NotNull(message = "댓글 내용은 비어 있을 수 없습니다.")
        String content
) {}


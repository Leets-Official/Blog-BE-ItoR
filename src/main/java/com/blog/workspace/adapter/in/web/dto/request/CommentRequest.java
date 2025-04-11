package com.blog.workspace.adapter.in.web.dto.request;

public record CommentRequest(
        Long postId,
        String content
) {}


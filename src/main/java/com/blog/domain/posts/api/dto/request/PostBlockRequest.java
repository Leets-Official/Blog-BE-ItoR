package com.blog.domain.posts.api.dto.request;

public record PostBlockRequest(
        String content,
        String imageUrl
) {
}

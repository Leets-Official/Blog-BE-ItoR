package com.blog.domain.posts.api.dto.request;

import java.util.List;

public record PostCreateRequest(
        String subject,
        List<PostBlockRequest> blocks
) {
}

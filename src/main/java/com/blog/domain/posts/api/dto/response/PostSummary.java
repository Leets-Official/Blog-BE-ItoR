package com.blog.domain.posts.api.dto.response;

import java.time.LocalDateTime;

public record PostSummary(
        int postId,
        String nickname,
        String subject,
        LocalDateTime createdAt
) {
}

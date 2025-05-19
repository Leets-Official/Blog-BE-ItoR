package com.blog.domain.posts.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record PostSummary(
        int postId,
        String nickname,
        String subject,
        List<PostBlockResponse> block,
        LocalDateTime createdAt
) {
}

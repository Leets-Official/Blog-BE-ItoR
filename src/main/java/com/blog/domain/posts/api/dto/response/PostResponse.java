package com.blog.domain.posts.api.dto.response;

import com.blog.domain.posts.domain.PostBlocks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        int postId,
        String nickname,
        String subject,
        LocalDateTime createdAt,
        List<PostBlocks> blockList
) {
}

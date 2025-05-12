package com.blog.domain.posts.api.dto.response;

import com.blog.domain.posts.domain.PostBlocks;

import java.util.List;

public record PostBlockResponse(
        String content,
        String imageUrl
) {
}

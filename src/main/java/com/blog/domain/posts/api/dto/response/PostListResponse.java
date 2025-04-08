package com.blog.domain.posts.api.dto.response;

import java.util.List;

public record PostListResponse(
        List<PostSummary> posts
) {
}

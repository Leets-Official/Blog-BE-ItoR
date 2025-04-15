package com.blog.domain.comments.api.dto.request;

public record CommentsRequest(
        int postId,
        String content

) {
}

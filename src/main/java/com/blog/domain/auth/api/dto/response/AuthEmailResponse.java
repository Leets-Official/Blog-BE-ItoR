package com.blog.domain.auth.api.dto.response;

public record AuthEmailResponse(
        boolean success,
        String message,
        Integer id
) {
}

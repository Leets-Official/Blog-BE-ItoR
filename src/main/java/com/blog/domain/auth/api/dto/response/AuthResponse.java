package com.blog.domain.auth.api.dto.response;

public record AuthResponse(
        boolean success,
        String message,
        Integer id
) {
}

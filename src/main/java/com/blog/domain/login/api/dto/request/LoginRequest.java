package com.blog.domain.login.api.dto.request;

public record LoginRequest(
        String email,
        String password,
        String refreshToken
) {
}

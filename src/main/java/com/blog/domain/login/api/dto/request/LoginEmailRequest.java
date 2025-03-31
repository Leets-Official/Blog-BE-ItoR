package com.blog.domain.login.api.dto.request;

public record LoginEmailRequest(
        String email,
        String password,
        String refreshToken
) {
}

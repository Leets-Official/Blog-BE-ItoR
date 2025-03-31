package com.blog.domain.login.api.dto.response;

public record LoginResponse(
        String refreshToken,
        String accessToken
) {
}

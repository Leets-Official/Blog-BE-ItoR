package com.blog.domain.login.api.dto.request;

public record LoginKakaoRequest(
        String accessToken,
        String refreshToken
) {
}

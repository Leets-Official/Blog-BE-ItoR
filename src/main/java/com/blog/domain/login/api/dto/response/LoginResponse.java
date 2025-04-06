package com.blog.domain.login.api.dto.response;

import com.blog.domain.users.domain.Users;

public record LoginResponse(
        String refreshToken,
        String accessToken,
        Users user
) {
}

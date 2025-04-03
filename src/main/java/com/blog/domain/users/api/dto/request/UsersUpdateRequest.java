package com.blog.domain.users.api.dto.request;

public record UsersUpdateRequest(
        int userId,
        String password,
        String nickname,
        String profileImage
) {
}

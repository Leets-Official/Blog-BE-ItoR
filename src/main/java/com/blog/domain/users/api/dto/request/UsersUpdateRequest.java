package com.blog.domain.users.api.dto.request;

import java.time.LocalDate;

public record UsersUpdateRequest(
        int user_id,
        String password,
        String nickname,
        String profile_image
) {
}

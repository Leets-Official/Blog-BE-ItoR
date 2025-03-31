package com.blog.domain.users.api.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsersInfoResponse(
        int user_id,
        String email,
        String password,
        String name,
        String nickname,
        LocalDate birth,
        String profile_image,
        Boolean social,
        String introduce
) {
}

package com.blog.domain.auth.api.dto.request;

import java.time.LocalDate;

public record AuthEmailRequest(
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

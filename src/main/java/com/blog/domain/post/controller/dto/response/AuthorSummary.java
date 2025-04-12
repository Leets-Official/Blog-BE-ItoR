package com.blog.domain.post.controller.dto.response;

import com.blog.domain.user.domain.User;

public record AuthorSummary(
        String profileImage,
        String nickname,
        String introduction
) {
    static AuthorSummary toAuthorSummary(User user) {
        return new AuthorSummary(
                user.getProfileImage(),
                user.getNickname(),
                user.getIntroduction()
        );
    }
}



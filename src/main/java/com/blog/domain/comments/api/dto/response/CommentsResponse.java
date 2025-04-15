package com.blog.domain.comments.api.dto.response;

import com.blog.domain.comments.domain.Comments;
import com.blog.domain.users.domain.Users;

import java.util.List;

public record CommentsResponse(
        String nickname,
        String profileImage,
        Comments comment
) {
}

package com.blog.workspace.adapter.in.web.dto.response;

import com.blog.workspace.domain.comment.Comment;
import com.blog.workspace.domain.user.User;

import java.time.format.DateTimeFormatter;
import java.util.*;

public record CommentResponse(
        Long id,
        UserPostResponse user,
        String comment,
        String createdAt ) {

    public static CommentResponse from(Comment comment, User user) {

        // 유저 관련 정보 처리
        UserPostResponse userResponse = UserPostResponse.from(user);

        //  날짜 관련 정보 처리
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd.yyyy.", Locale.ENGLISH);
        String formattedDate = comment.getCreated().format(formatter);

        return new CommentResponse(comment.getId(), userResponse, comment.getContent(), formattedDate);
    }

}

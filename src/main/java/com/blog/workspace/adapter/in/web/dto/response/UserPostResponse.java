package com.blog.workspace.adapter.in.web.dto.response;

import com.blog.workspace.domain.user.User;

public class UserPostResponse {

    private String author;

    private String authorThumbnail;

    // 생성자
    public UserPostResponse(String author, String authorThumbnail) {
        this.author = author;
        this.authorThumbnail = authorThumbnail;
    }

    /// 정적 팩토리 메서드
    public static UserPostResponse from(User user) {

        String author = user.getNickname() == null ? user.getUsername() : user.getNickname();

        return new UserPostResponse(author, user.getImageUrl());
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorThumbnail() {
        return authorThumbnail;
    }
}

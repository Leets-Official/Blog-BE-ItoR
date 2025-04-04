package com.blog.workspace.adapter.in.web.dto.response;

import com.blog.workspace.domain.user.User;

public class UserResponse {
    /*
        닉네임, 한줄 소개
     */

    private final Long id;
    private final String nickname;
    private final String description;

    public UserResponse(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.description = user.getDescription();
    }

    /// @Getter
    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDescription() {
        return description;
    }
}

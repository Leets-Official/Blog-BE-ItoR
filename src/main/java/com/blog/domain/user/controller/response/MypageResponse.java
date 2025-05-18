package com.blog.domain.user.controller.response;

import com.blog.domain.user.domain.User;

public record MypageResponse (
        String userName,
        String nickName,
        String email,
        String profileImageUrl,
        String introduction

){
    public static MypageResponse from(User user) {
        return new MypageResponse(
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImage(),
                user.getIntroduction()
        );
    }
}

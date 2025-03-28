package com.blog.workspace.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthFirstTimeRequest {

    @NotBlank(message = "nickname은 반드시 입력해야하는 필수 사항입니다!")
    @Size(max = 20, message = "닉네임은 최대 20글자 입니다.")
    private String nickname;

    private String imageUrl;

    @NotBlank(message = "description는 반드시 입력해야하는 필수 사항입니다!")
    private String description;

    @NotBlank(message = "descripbirthdaytion는 반드시 입력해야하는 필수 사항입니다!")
    private String birthday;

    public String getNickname() {
        return nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getBirthday() {
        return birthday;
    }

}

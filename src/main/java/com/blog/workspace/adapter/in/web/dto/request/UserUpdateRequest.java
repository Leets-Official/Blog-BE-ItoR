package com.blog.workspace.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class UserUpdateRequest {


    @NotBlank(message = "nickname은 반드시 입력해야하는 필수 사항입니다!")
    @Size(max = 20, message = "닉네임은 최대 20글자 입니다.")
    private String nickname;

    @NotBlank(message = "password는 반드시 입력해야하는 필수 사항입니다!")
    private String password;

    @NotBlank(message = "passwordCheck는 반드시 입력해야하는 필수 사항입니다!")
    private String passwordCheck;

    private MultipartFile imageUrl;

    @NotBlank(message = "description는 반드시 입력해야하는 필수 사항입니다!")
    private String description;

    @NotBlank(message = "descripbirthdaytion는 반드시 입력해야하는 필수 사항입니다!")
    private String birthday;

    public UserUpdateRequest(String nickname, String password, String passwordCheck, MultipartFile imageUrl, String description, String birthday) {
        this.nickname = nickname;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.imageUrl = imageUrl;
        this.description = description;
        this.birthday = birthday;
    }


    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public MultipartFile getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }
}

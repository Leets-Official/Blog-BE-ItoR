package com.blog.workspace.adapter.in.web.dto.request;

import jakarta.validation.constraints.*;

public class UserRegisterRequest {

    @NotNull(message = "email은 반드시 입력해야하는 필수 사항입니다!")
    @Email(message = "이메일 형식이 적합하지 않습니다")
    private String email;

    @NotNull(message = "username은 반드시 입력해야하는 필수 사항입니다!")
    @Size(max = 10, message = "이름은 최대 10글자 입니다")
    private String username;

    @NotNull(message = "nickname은 반드시 입력해야하는 필수 사항입니다!")
    @Size(max = 20, message = "닉네임은 최대 20글자 입니다.")
    private String nickname;

    @NotNull(message = "password는 반드시 입력해야하는 필수 사항입니다!")
    private String password;

    @NotNull(message = "passwordCheck는 반드시 입력해야하는 필수 사항입니다!")
    private String passwordCheck;

    private String imageUrl;

    @NotNull(message = "description는 반드시 입력해야하는 필수 사항입니다!")
    private String description;

    @NotNull(message = "birthday는 반드시 입력해야하는 필수 사항입니다!")
    private String birthday;


    // Getter
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordCheck() {
        return passwordCheck;
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

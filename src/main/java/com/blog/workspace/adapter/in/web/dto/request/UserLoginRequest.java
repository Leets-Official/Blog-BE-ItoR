package com.blog.workspace.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserLoginRequest {

    @NotNull(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식을 입력해 주세요.")
    private String email;

    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

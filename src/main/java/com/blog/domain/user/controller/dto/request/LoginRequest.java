package com.blog.domain.user.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    private String email;
    private String password;

    @Email
    @NotBlank
    public String getEmail() {
        return email;
    }

    @NotBlank
    public String getPassword() {
        return password;
    }
}

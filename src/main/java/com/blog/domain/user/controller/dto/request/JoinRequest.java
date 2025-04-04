package com.blog.domain.user.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class JoinRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Size(max = 10)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String nickname;

    @NotNull
    private LocalDate birth;

    @NotBlank
    @Size(max = 30)
    private String introduction;

    private String profileImage;

    private String provider = "email";

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getProvider() {
        return provider;
    }
}

package com.blog.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSignupRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;
    @JsonProperty("birth_date")
    private String birthDate;
    @JsonProperty("profile_image")
    private String profileImage;
    private String introduction;
    @JsonProperty("auth_method")
    private String authMethod;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getNickname() { return nickname; }
    public String getBirthDate() { return birthDate; }
    public String getProfileImage() { return profileImage; }
    public String getIntroduction() { return introduction; }
    public String getAuthMethod() { return authMethod; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public void setAuthMethod(String authMethod) { this.authMethod = authMethod; }
}

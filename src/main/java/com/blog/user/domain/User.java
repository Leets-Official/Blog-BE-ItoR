package com.blog.user.domain;

import java.sql.Timestamp;

public class User {

    private Long userId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String birthDate;
    private String profileImage;
    private String introduction;
    private String authMethod;
    private final Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isActive;

    public User(Long userId, String email, String password, String name, String nickname, String birthDate, String profileImage, String introduction, String authMethod, Timestamp createdAt, Timestamp updatedAt, boolean isActive) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.authMethod = authMethod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) { this.userId = userId; }

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

    public String getBirthDate() {
        return birthDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getAuthMethod() {
        return authMethod;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

}

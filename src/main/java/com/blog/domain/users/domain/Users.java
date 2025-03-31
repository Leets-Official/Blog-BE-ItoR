package com.blog.domain.users.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Users {
    private int id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private LocalDate birth;
    private String profile_image;
    private Boolean social;
    private String introduce;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // 이메일 회원가입
    public Users(String email, String password, String name, String nickname, LocalDate birth,
                 String profile_image, Boolean social, String introduce) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.profile_image = profile_image;
        this.social = social;
        this.introduce = introduce;
    }

    // 카카오 회원가입
    public Users(String email, String name, String nickname, LocalDate birth, String introduce) {
        this.email = email;
        this.password = "social";
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.profile_image = null;
        this.social = true;
        this.introduce = introduce;
    }

    // JDBC -> 도메인
    public Users(int id, String email, String name, String nickname, String password, String profile_image,
                 Boolean social, String introduce, LocalDate birth, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.profile_image = profile_image;
        this.social = social;
        this.introduce = introduce;
        this.birth = birth;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getter & Setter
    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeProfileImage(String profile_image) {
        this.profile_image = profile_image;
    }

    public void changeIntroduce(String introduce) {
        this.introduce = introduce;
    }

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

    public String getProfileImage() {
        return profile_image;
    }

    public boolean isSocial() {
        return social;
    }

    public String getIntroduce() {
        return introduce;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public LocalDateTime getUpdatedAt() {
        return updated_at;
    }
}



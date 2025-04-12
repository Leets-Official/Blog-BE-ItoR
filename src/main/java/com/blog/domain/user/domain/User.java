package com.blog.domain.user.domain;

import com.blog.global.common.BaseDomain;

import java.time.LocalDateTime;

public class User extends BaseDomain {


    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private LocalDateTime birth;
    private String introduction;
    private String profileImage;
    private Provider provider;

    public User() {
    }

    public User(String email, String password, String name, String nickname, LocalDateTime birth, String introduction, String profileImage, String provider) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.introduction = introduction;
        this.profileImage = profileImage;
        this.provider = Provider.valueOf(provider);
    }



    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // 🔹 정적 팩토리 메서드 추가 (더 명확한 의미 전달)
    public static User createSocialUser(String email, String password, String name, String nickname, LocalDateTime birth, String introduction, String profileImage, String provider) {
        return new User(email, password, name, nickname, birth, introduction, profileImage, provider);
    }

    public static User createBasicUser(Long id, String email, String password) {
        return new User(id, email, password);
    }



    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public String getNickname() {
        return nickname;
    }


    public String getProfileImage() {
        return profileImage;
    }
    public String getName() {
        return name;
    }

    public LocalDateTime getBirth() {
        return birth;
    }

    public String getIntroduction() {
        return introduction;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

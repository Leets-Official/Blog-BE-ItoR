package com.blog.domain.user.domain;

import com.blog.global.common.BaseDomain;

public class User extends BaseDomain {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String birth;
    private String introduction;
    private String profileImageUrl;
    private Provider provider;

    public User() {
    }

    public User(Long id, String email, String password, String name, String nickname, String birth, String introduction, String profileImageUrl, Provider provider) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public User(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
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


    public String getProfileImageUrl() {
        return profileImageUrl;
    }


}

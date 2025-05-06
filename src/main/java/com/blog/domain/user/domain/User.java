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

    // constructor
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

    public User(long id, String email, String password, String name, String nickname, LocalDateTime localDateTime, String introduction, String profileImage, String provider) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = localDateTime;
        this.introduction = introduction;
        this.profileImage = profileImage;
        this.provider = Provider.valueOf(provider);
    }

    public User(String email, String password, String name, String nickname, LocalDateTime birth, String introduction, String profileImage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.introduction = introduction;
        this.profileImage = profileImage;
    }

   // getter
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

}

package com.blog.workspace.domain.user;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class User extends BaseDomain {

    private Long id;

    private final String email;


    private final String nickname;

    private final String password;

    private final String imageUrl;

    private final Social social;

    /// 생성자
    public User(String email, String nickname, String password, String imageUrl, Social social, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.imageUrl = imageUrl;
        this.social = social;
    }

    /// @Getter
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Social getSocial() {
        return social;
    }

    // 비즈니스 로직
}

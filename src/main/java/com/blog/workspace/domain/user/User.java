package com.blog.workspace.domain.user;

import com.blog.workspace.domain.BaseDomain;

import java.time.LocalDateTime;

public class User extends BaseDomain {

    private Long id;

    private final String email;

    private final String username;

    private final String nickname;

    private final String password;

    private final String imageUrl;

    private final boolean social;

    private final String description;

    private final String birthday;

    /// 서비스 내부 생성자
    public User(String email, String nickname, String username, String password, String imageUrl, boolean social, String description, String birthday, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.email = email;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.imageUrl = imageUrl;
        this.social = social;
        this.description = description;
        this.birthday = birthday;
    }

    /// JDBC에서 Domain으로 변환할 때 생성자
    public User(Long id, String email, String username, String nickname, String password, String imageUrl, boolean social, String description, String birthday, LocalDateTime createdAt, LocalDateTime updatedAt) {

        super(createdAt, updatedAt);
        this.id = id;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.imageUrl = imageUrl;
        this.social = social;
        this.description = description;
        this.birthday = birthday;
    }
    /// 비즈니스 로직



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

    public String getUsername() {
        return username;
    }

    public boolean isSocial() {
        return social;
    }

    public String getDescription() {
        return description;
    }

    public String getBirthday() {
        return birthday;
    }


}

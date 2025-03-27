package com.blog.workspace.adapter.out.jdbc.user;

import com.blog.workspace.domain.user.User;

import java.time.LocalDateTime;

public class UserJdbc  {

    private Long id;
    private final String email;
    private final String username;
    private final String nickname;
    private final String password;
    private final String imageUrl;
    private final boolean social;
    private final String description;
    private final String birthday;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // 생성자, Id는 MYSQL 내부에서 자동으로 증가되기에 제거,
    // 시간은 서비스를 요청할 때 생성된다.
    public UserJdbc(String email, String username, String nickname, String password, String imageUrl, boolean social, String description, String birthday, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.imageUrl = imageUrl;
        this.social = social;
        this.description = description;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // from
    public static UserJdbc from(User user) {
        return new UserJdbc(
                user.getEmail(),
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getImageUrl(),
                user.isSocial(),
                user.getDescription(),
                user.getBirthday(),
                user.getCreated(),
                user.getUpdated()
        );
    }

    // toDomain
    public User toDomain() {
        return new User(
                this.id,
                this.email,
                this.username,
                this.nickname,
                this.password,
                this.imageUrl,
                this.social,
                this.description,
                this.birthday,
                this.createdAt,
                this.updatedAt
        );
    }

    /// @Getter
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
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

    public boolean isSocial() {
        return social;
    }

    public String getDescription() {
        return description;
    }

    public String getBirthday() {
        return birthday;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /// @ 부가적인 기능을 위한 생성자 모음
    // Repository에서 가져오기 위한 생성자이다.
    public UserJdbc(Long id, String email, String username, String nickname, String password, String imageUrl, boolean social, String description, String birthday, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.imageUrl = imageUrl;
        this.social = social;
        this.description = description;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

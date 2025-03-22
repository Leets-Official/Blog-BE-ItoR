package com.blog.workspace.adapter.out.jdbc.user;

import com.blog.workspace.domain.user.Social;
import com.blog.workspace.domain.user.User;

import java.time.LocalDateTime;

public class UserJdbc  {

    private Long id;
    private final String email;
    private final String nickname;
    private final String password;
    private final String imageUrl;
    private final Social social;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // 생성자, Id는 MYSQL 내부에서 자동으로 증가되기에 제거
    public UserJdbc(String email, String nickname, String password, String imageUrl, Social social, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.imageUrl = imageUrl;
        this.social = social;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // from
    public static UserJdbc from(User user) {
        return new UserJdbc(
                user.getEmail(),
                user.getNickname(),
                user.getPassword(),
                user.getImageUrl(),
                user.getSocial(),
                user.getCreated(),
                user.getUpdated()
        );
    }

    // toDomain
    public User toDomain() {
        return new User(
                this.id,
                this.email,
                this.nickname,
                this.password,
                this.imageUrl,
                this.social,
                this.createdAt,
                this.updatedAt
        );
    }
}

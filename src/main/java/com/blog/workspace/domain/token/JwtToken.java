package com.blog.workspace.domain.token;

import java.time.LocalDateTime;

public class JwtToken {

    /*
        RefreshToken 으로 accessToken을 재발급하기 위한 엔티티입니다.
     */

    private Long id;
    private final Long userId;
    private final String refreshToken;
    private final LocalDateTime expiredAt;

    // 생성자
    public JwtToken(Long userId, String refreshToken, LocalDateTime expiredAt) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }


    // Getter 메서드
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    /// JDBC에서 사용할 생성자
    // 생성자
    public JwtToken(Long id, Long userId, String refreshToken, LocalDateTime expiredAt) {
        this.id = id;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }
}

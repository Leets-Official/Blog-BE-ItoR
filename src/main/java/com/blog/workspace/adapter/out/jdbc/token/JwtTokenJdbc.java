package com.blog.workspace.adapter.out.jdbc.token;


import com.blog.workspace.domain.token.JwtToken;

import java.time.LocalDateTime;

public class JwtTokenJdbc {

    private Long id;
    private Long userId;
    private String refreshToken;
    private LocalDateTime expiredAt;


    // 생성자, Id는 DB에서 자동 증가
    public JwtTokenJdbc(Long userId, String refreshToken, LocalDateTime expiredAt) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }

    // from
    public static JwtTokenJdbc from(JwtToken jwtToken) {
        return new JwtTokenJdbc(
                jwtToken.getUserId(),
                jwtToken.getRefreshToken(),
                jwtToken.getExpiredAt()
        );
    }

    // toDomain
    public JwtToken toDomain() {
        return new JwtToken(
                this.id,
                this.userId,
                this.refreshToken,
                this.expiredAt
        );
    }

    // Getter 메서드들 (필요 시)
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


    /// Repo에서 사용할 생성자
    public JwtTokenJdbc(Long id, Long userId, String refreshToken, LocalDateTime expiredAt) {
        this.id = id;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }
}

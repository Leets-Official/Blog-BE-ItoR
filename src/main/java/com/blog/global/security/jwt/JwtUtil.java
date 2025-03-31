package com.blog.global.security.jwt;

import com.blog.domain.users.domain.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    @Value("${jwt.access-token-expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshExpiration;

    // 시크릿 키 생성
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 액세스 토큰 생성
    public String createAccessToken(Users user) {
        return Jwts.builder()
                .claim("user_id", user.getUserId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

    // 리프레쉬 토큰 생성
    public String createRefreshToken(Users user) {
        return Jwts.builder()
                .claim("user_id", user.getUserId())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey)
                .compact();
    }

    // 토큰이 유효한지 검사
    public boolean isValidRefreshToken(String refreshToken) {
        try {
            getClaimsToken(refreshToken);
            return true;
        } catch (NullPointerException | JwtException e) {
            return false;
        }
    }

    // 토큰 정보 조회
    private Claims getClaimsToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

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
        String role = user.getUserId() == 1 ? "ADMIN" : "USER";

        return Jwts.builder()
                .claim("user_id", user.getUserId())
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

    // 리프레쉬 토큰 생성
    public String createRefreshToken(Users user) {
        String role = user.getUserId() == 1 ? "ADMIN" : "USER";

        return Jwts.builder()
                .claim("user_id", user.getUserId())
                .claim("role", role)
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

    // 토큰에서 role을 추출하는 메서드 추가
    public String getUserRole(String token) {
        try {
            Claims claims = getClaimsToken(token); // 토큰에서 Claims 가져오기
            return claims.get("role", String.class); // "role" 클레임에서 역할 정보 추출
        } catch (Exception e) {
            return null; // 만약 에러가 발생하면 null 반환
        }
    }
}

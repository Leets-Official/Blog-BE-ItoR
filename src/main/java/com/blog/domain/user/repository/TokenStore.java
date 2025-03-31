package com.blog.domain.user.repository;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenStore {

    // 인메모리 저장소
    private final Map<Long, String> tokenStore = new ConcurrentHashMap<>();

    // 생성자에서 테스트용 사용자 등록 (암호는 "password")
    public TokenStore() {
        // 예시 사용자: 이메일 "test@example.com", id=1, 이름 "Test User"
        tokenStore.put(3L,"refreshToken");
    }
    /**
     * ✅ Refresh Token 저장
     */
    public void storeToken(Long userId, String token) {
        tokenStore.put(userId, token);
    }

    /**
     * ✅ Refresh Token 삭제
     */
    public void removeToken(Long userId) {
        tokenStore.remove(userId);
    }

    /**
     * ✅ Refresh Token 조회
     */
    public Optional<String> getToken(Long userId) {
        return Optional.ofNullable(tokenStore.get(userId));
    }
    /**
     * ✅ Refresh Token 검증 (서버에 저장된 토큰과 비교)
     */
    public boolean isRefreshTokenStoredInServer(Long userId, String token) {
        return getToken(userId).map(storedToken -> storedToken.equals(token)).orElse(false);
    }

}

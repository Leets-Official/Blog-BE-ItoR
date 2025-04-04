package com.blog.domain.user.repository;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenStore {

    // 인메모리 저장소
    private final Map<Long, String> tokenStore = new ConcurrentHashMap<>();

    public TokenStore() {
    }

    // Refresh Token 저장
    public void storeToken(Long userId, String token) {
        tokenStore.put(userId, token);
    }

    // Refresh Token 삭제
    public void removeToken(Long userId) {
        tokenStore.remove(userId);
    }

    //  Refresh Token 조회
    public Optional<String> getToken(Long userId) {
        return Optional.ofNullable(tokenStore.get(userId));
    }

}

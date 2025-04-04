package com.blog.token.domain;

public interface TokenRepository {
    void save(Token token);
    Token findByUserId(Long userId);
    void deleteByUserId(Long userId);
}

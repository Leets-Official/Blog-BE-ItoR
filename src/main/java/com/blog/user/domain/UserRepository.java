package com.blog.user.domain;

public interface UserRepository {
    User findByEmail(String email); // 로그인용
    User findById(Long userId);     // 유저 조회용
    void save(User user);           // 회원가입용
    void update(User user);         // 정보 수정용
    void softDelete(Long userId);   // 탈퇴용
}

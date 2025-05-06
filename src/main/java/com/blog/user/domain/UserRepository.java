package com.blog.user.domain;

import java.sql.Timestamp;

public interface UserRepository {
    User findByEmail(String email); // 로그인용
    User findById(Long userId);     // 유저 조회용
    void save(User user);           // 회원가입용
    void update(Long userId, String nickname, String profileImage, Timestamp updatedAt);         // 정보 수정용
    void softDelete(Long userId);   // 탈퇴용
}

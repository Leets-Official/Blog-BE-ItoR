package com.blog.workspace.application.out.user;

import com.blog.workspace.domain.user.User;

import java.util.Optional;

public interface UserPort {

    /// 저장
    // 저장하기
    User saveUser(User user);

    // 수정하기
    User updateUser(User user);

    /// 조회하기

    boolean loadExistingEmail(String email);

    Optional<User> findMe(Long userId);
}

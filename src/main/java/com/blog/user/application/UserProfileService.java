package com.blog.user.application;

import com.blog.user.presentation.dto.UserResponse;
import com.blog.user.presentation.dto.UserUpdateRequest;

//사용자 정보 관리 전용 인터페이스
public interface UserProfileService {
    UserResponse getUser(Long userId); // 조회
    UserResponse updateUser(Long userId, UserUpdateRequest request); // 수정
    void deleteUser(Long userId); // 삭제
}

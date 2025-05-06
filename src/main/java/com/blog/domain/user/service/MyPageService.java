package com.blog.domain.user.service;

import com.blog.domain.user.controller.request.UpdateRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyPageService {

    private final UserRepository userRepository;

    public MyPageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //정보 조회
    public User getMyPageInfo(long userId) {
        return userRepository.findByUserIdForMyPage(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    }

    //정보 수정
    @Transactional
    public User updateMyPageInfo(long userId, UpdateRequest req) {
        User existingUser = userRepository.findByUserIdForMyPage(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 변경 사항만 반영
        existingUser.updateMyPageInfo(req);

        return userRepository.updateUser(existingUser);
    }


}

package com.blog.domain.user.service;

import com.blog.domain.user.controller.dto.request.UpdateRequest;
import com.blog.domain.user.domain.Provider;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

        User updatedUser = mergeUserInfo(existingUser, req);

        return userRepository.updateUser(updatedUser);
    }

    private User mergeUserInfo(User old, UpdateRequest req) {
        return new User(
                req.name() != null ? req.name() : old.getName(),
                req.email() != null ? req.email() : old.getEmail(),
                req.password() != null ? req.password() : old.getPassword(),
                req.nickname() != null ? req.nickname() : old.getNickname(),
                req.birth() != null ? req.birth() : old.getBirth(),
                req.profileImage() != null ? req.profileImage() : old.getProfileImage(),
                req.introduction() != null ? req.introduction() : old.getIntroduction()
        );
    }


}

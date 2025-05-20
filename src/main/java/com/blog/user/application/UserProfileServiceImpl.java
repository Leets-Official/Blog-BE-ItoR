package com.blog.user.application;

import com.blog.user.domain.User;
import com.blog.user.domain.UserRepository;
import com.blog.user.presentation.dto.UserResponse;
import com.blog.user.presentation.dto.UserUpdateRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    public UserProfileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null || !user.isActive()) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setNickname(user.getNickname());
        response.setProfileImage(user.getProfileImage());
        response.setCreatedAt(user.getCreatedAt().toString());

        return response;
    }

    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        userRepository.update(userId, request.getNickname(), request.getProfileImage(), updatedAt);

        User updated = userRepository.findById(userId);
        if (updated == null) {
            throw new RuntimeException("사용자 정보를 수정할 수 없습니다.");
        }

        UserResponse response = new UserResponse();
        response.setUserId(updated.getUserId());
        response.setEmail(updated.getEmail());
        response.setNickname(updated.getNickname());
        response.setProfileImage(updated.getProfileImage());
        response.setUpdatedAt(updated.getUpdatedAt().toString());

        return response;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.softDelete(userId);
    }
}

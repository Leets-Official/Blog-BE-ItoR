package com.blog.domain.user.service;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isEmailUsed(String email) {
        return userRepository.existsByEmail(email);
    }

    public void join(JoinRequest joinRequest) {
        userRepository.save(joinRequest);
    }

    public Optional<User> findByKakaoInfo(Map<String, Object> userInfo) {
        Optional<User> existUser=userRepository.findByEmail((String) userInfo.get("email"));
        return existUser;
    }

}

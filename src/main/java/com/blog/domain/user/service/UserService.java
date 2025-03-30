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
        // "kakao_account" 내에서 "email" 가져오기
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");

        if (kakaoAccount == null || !kakaoAccount.containsKey("email")) {
            System.out.println("이메일 정보가 없습니다.");
            return Optional.empty(); // 이메일이 없으면 회원 조회 불가능
        }

        String email = (String) kakaoAccount.get("email");
        System.out.println("카카오에서 가져온 이메일: " + email);

        return userRepository.findByEmail(email);
    }


}

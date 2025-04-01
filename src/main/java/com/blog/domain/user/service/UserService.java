package com.blog.domain.user.service;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.security.PasswordUtil;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    public void join(JoinRequest joinRequest) throws NoSuchAlgorithmException {

        String encryptedPassword = PasswordUtil.encryptPassword(joinRequest.getPassword());
        System.out.println(joinRequest.getProvider());
        User joinUser = new User(
                joinRequest.getEmail(),  // email 추가
                encryptedPassword,
                joinRequest.getName(),
                joinRequest.getNickname(),
                joinRequest.getBirth(),
                joinRequest.getIntroduction(),
                joinRequest.getProfileImage(),
                joinRequest.getProvider()
                );
        System.out.println(joinUser);
        userRepository.save(joinUser);
    }

    // userRepository에서 userInfo조회
    public Optional<User> findByKakaoInfo(Map<String, Object> userInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");

        if (kakaoAccount == null || !kakaoAccount.containsKey("email")) {
            return Optional.empty(); 
        }

        String email = (String) kakaoAccount.get("email");
        return userRepository.findByEmail(email);
    }

    // 존재하는 이메일 여부 확인
    public boolean isEmailUsed(String email) {
        return userRepository.existsByEmail(email);
    }

}

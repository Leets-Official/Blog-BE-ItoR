package com.blog.domain.user.service;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.response.ApiResponse;
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

        String encryptedPassword = PasswordUtil.encryptPassword(joinRequest.password());

        /// 이메일 중복 체크
        boolean emailUsed = isEmailUsed(joinRequest.email());

        if (emailUsed) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User joinUser = createMember(joinRequest, encryptedPassword);

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
    private boolean isEmailUsed(String email) {
        return userRepository.existsByEmail(email);
    }

    // 유저 만들기
    private User createMember(JoinRequest joinRequest, String encryptedPassword) {
        return new User(
                joinRequest.email(),  // email 추가
                encryptedPassword,
                joinRequest.name(),
                joinRequest.nickname(),
                joinRequest.birth(),
                joinRequest.introduction(),
                joinRequest.profileImage(),
                joinRequest.provider()
        );
    }

}

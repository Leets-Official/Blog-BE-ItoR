package com.blog.domain.user.service;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.security.CustomTokenUtil;
import com.blog.global.security.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginService {

    private final UserRepository userRepository;
    // Refresh Token을 저장할 인메모리 저장소 (실제 운영에서는 DB 또는 Redis 사용)
    private final ConcurrentHashMap<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    public LoginService(UserRepository userRepository) {

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

    //회원가입 시 이메일 중복 체크, 비밀번호 변경 시 현재 비밀번호 확인,OAuth 로그인 (소셜 로그인) 검증
    public User authenticateUser(String email, String password) {
        // 1️⃣ 이메일이 존재하는지 확인
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User user = optionalUser.get(); // 이메일이 존재하면 사용자 정보 가져오기

        // 2️⃣ 비밀번호가 일치하는지 확인 (암호화된 비밀번호와 입력된 비밀번호 비교)
        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        return user; // 검증 완료된 사용자 반환
    }


    // Refresh Token을 사용하여 새로운 Access Token 발급
    public String refreshAccessToken(String refreshToken) {
        if (CustomTokenUtil.validateToken(refreshToken)) {

            Map<String, Object> userInfo = CustomTokenUtil.getUserFromToken(refreshToken);

            return CustomTokenUtil.generateAccessToken((Long) userInfo.get("userId"), (String) userInfo.get("email"));
        } else {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }


}

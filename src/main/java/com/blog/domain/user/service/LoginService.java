package com.blog.domain.user.service;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import com.blog.global.security.CustomTokenUtil;
import com.blog.global.security.PasswordUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    public LoginService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    //회원가입 시 이메일 중복 체크, 비밀번호 변경 시 현재 비밀번호 확인,OAuth 로그인 (소셜 로그인) 검증
    public User authenticateUser(String email, String password) {
        System.out.println("서비스 호출 - 이메일: " + email + ", 비밀번호: " + password);

        // 1️⃣ 이메일이 존재하는지 확인 (orElseThrow를 사용하여 바로 예외 처리)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        System.out.println("이메일: " + user.getEmail());
        System.out.println("비밀번호: " + user.getPassword());

        // 2️⃣ 비밀번호가 일치하는지 확인 (한 번 가져온 user 객체 사용)
        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        System.out.println("비밀번호 확인 완료");
        return user; // 검증 완료된 사용자 반환
    }




}

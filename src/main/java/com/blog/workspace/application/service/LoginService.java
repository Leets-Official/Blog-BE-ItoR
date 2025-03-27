package com.blog.workspace.application.service;

import com.blog.workspace.adapter.in.web.dto.request.UserLoginRequest;
import com.blog.workspace.application.in.user.LoginUseCase;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.domain.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class LoginService implements LoginUseCase {

    private final UserPort userPort;
    private final HttpSession session;

    /// 생성자
    public LoginService(UserPort userPort, HttpSession session) {
        this.userPort = userPort;
        this.session = session;
    }

    @Override
    public boolean login(UserLoginRequest request) {

        /// 예외처리
        User user = userPort.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("가입되지 않은 이메일 입니다”"));

        /// 비밀번호 검증하기
        if (!user.getPassword().equals(request.getPassword())) {
            return false;
        }

        // 세션에 사용자 정보 저장
        session.setAttribute("user", user);  // 세션에 로그인한 사용자 정보 저장

        return true;
    }
}

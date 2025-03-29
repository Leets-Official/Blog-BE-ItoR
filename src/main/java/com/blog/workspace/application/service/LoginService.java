package com.blog.workspace.application.service;

import com.blog.common.security.jwt.provider.JwtTokenProvider;
import com.blog.workspace.adapter.in.web.dto.request.UserLoginRequest;
import com.blog.workspace.adapter.in.web.dto.response.UserLoginResponse;
import com.blog.workspace.application.in.user.LoginUseCase;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.application.service.exception.NotEmailException;
import com.blog.workspace.application.service.exception.NotEqualLoginPassword;
import com.blog.workspace.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService implements LoginUseCase {

    private final UserPort userPort;

    /// 토큰 제공
    private final JwtTokenProvider jwtTokenProvider;

    /// 생성자
    public LoginService(UserPort userPort, JwtTokenProvider jwtTokenProvider) {
        this.userPort = userPort;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {

        /// 예외처리
        User user = userPort.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new NotEmailException("가입되지 않은 이메일 입니다”"));

        /// 비밀번호 검증하기
        if (!user.getPassword().equals(request.getPassword())) {
            throw new NotEqualLoginPassword("“비밀번호가 일치하지 않습니다”");
        }

        // 토큰을 넘겨준다.
        String accessToken = jwtTokenProvider.createJwt(user);

        return new UserLoginResponse(user, accessToken, false);
    }
}

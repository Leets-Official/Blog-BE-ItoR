package com.blog.workspace.application.service;

import com.blog.common.security.jwt.provider.JwtTokenProvider;
import com.blog.workspace.adapter.in.web.dto.request.UserLoginRequest;
import com.blog.workspace.adapter.in.web.dto.response.UserLoginResponse;
import com.blog.workspace.application.in.user.LoginUseCase;
import com.blog.workspace.application.out.token.TokenPort;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.application.service.exception.NotEmailException;
import com.blog.workspace.application.service.exception.NotEqualLoginPassword;
import com.blog.workspace.domain.token.JwtToken;
import com.blog.workspace.domain.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.blog.common.security.jwt.provider.JwtTokenProvider.REFRESH_TOKEN_EXPIRATION_TIME;

@Service
@Transactional
public class LoginService implements LoginUseCase {

    private final UserPort userPort;

    /// 토큰 제공
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenPort tokenPort;


    /// 생성자
    public LoginService(UserPort userPort, JwtTokenProvider jwtTokenProvider, TokenPort tokenPort) {
        this.userPort = userPort;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenPort = tokenPort;
    }

    @Override
    public UserLoginResponse login(HttpServletResponse response,UserLoginRequest request) {

        /// 예외처리
        User user = userPort.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new NotEmailException("가입되지 않은 이메일 입니다”"));

        /// 비밀번호 검증하기
        if (!user.getPassword().equals(request.getPassword())) {
            throw new NotEqualLoginPassword("“비밀번호가 일치하지 않습니다”");
        }

        // 토큰을 넘겨준다.
        String accessToken = jwtTokenProvider.createAccessToken(user);
        createRefreshToken(response, user);

        return new UserLoginResponse(user, accessToken, false);
    }


    // 리프레쉬 토큰 생성 및 저장
    private void createRefreshToken(HttpServletResponse response, User user) {

        String refreshToken = jwtTokenProvider.createRefreshToken(response, user);

        // RefreshToken 만료 시간을 계산 (현재 시간 + REFRESH_TOKEN_EXPIRATION_TIME)
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_TIME);

        JwtToken jwtToken = new JwtToken(user.getId(), refreshToken, expiredAt);
        tokenPort.saveToken(jwtToken);
    }
}

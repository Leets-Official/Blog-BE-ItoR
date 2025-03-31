package com.blog.workspace.application.service;

import com.blog.security.jwt.provider.JwtTokenProvider;
import com.blog.workspace.adapter.in.web.dto.request.AuthFirstTimeRequest;
import com.blog.workspace.adapter.in.web.dto.response.UserLoginResponse;
import com.blog.workspace.adapter.out.oauth.OAuthUserInfo;
import com.blog.workspace.adapter.out.oauth.kakao.KaKaoApiClient;
import com.blog.workspace.application.in.auth.AuthUserUseCase;
import com.blog.workspace.application.out.auth.OAuthLoginParams;
import com.blog.workspace.application.out.token.TokenPort;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.domain.token.JwtToken;
import com.blog.workspace.domain.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.blog.security.jwt.provider.JwtTokenProvider.REFRESH_TOKEN_EXPIRATION_TIME;


@Service
@Transactional
public class AuthService implements AuthUserUseCase {

    private final KaKaoApiClient clients;
    private final UserPort userPort;

    ///  JWT 발급
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenPort tokenPort;

    public AuthService(KaKaoApiClient clients, UserPort userPort, JwtTokenProvider jwtTokenProvider, TokenPort tokenPort) {
        this.clients = clients;
        this.userPort = userPort;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenPort = tokenPort;
    }

    @Override
    public UserLoginResponse login(HttpServletResponse httpServletResponse, OAuthLoginParams params) {

        // 토큰 정보 받아오기
        String accessToken = clients.requestAccessToken(params);

        // 해당 토큰을 바탕으로 정보 요청
        OAuthUserInfo oAuthUserInfo = clients.requestOAuthInfo(accessToken);

        // 최초 로그인이 아니라면, 정보 반환
        if (!checkFirstLogin(oAuthUserInfo)) {
            User user = loadUser(oAuthUserInfo);// 기존 로그인 처리

            // accessToken을 생성한다.
            String token = jwtTokenProvider.createAccessToken(user);

            // refreshToken을 생성한다.
            createRefreshToken(httpServletResponse, user);

            return new UserLoginResponse(user, token, false);

        } else {
            // 저장하는 로직은 수행한다.
            User user = newUser(oAuthUserInfo);

            // JWT를 제공한다.
            String token = jwtTokenProvider.createAccessToken(user);
            createRefreshToken(httpServletResponse, user);

            return new UserLoginResponse(user, token, true);
        }

    }

    @Override
    public User updateInfo(String email, AuthFirstTimeRequest request) {

        User user = userPort.findUserByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("해당 유저는 존재하지 않습니다."));

        // 닉네임이 요청에 있으면 수정
        if (request.getNickname() != null && !request.getNickname().isEmpty()) {
            user.changeNickname(request.getNickname());
        }

        // 생일이 요청에 있으면 수정
        if (request.getBirthday() != null && !request.getBirthday().isEmpty()) {
            user.changeBirthday(request.getBirthday());
        }

        // 설명이 요청에 있으면 수정
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            user.changeDescription(request.getDescription());
        }

        // 이미지 URL이 요청에 있으면 수정
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            user.changeImageUrl(request.getImageUrl());
        }


        return userPort.updateUser(user);
    }

    // 최초 로그인 여부 체크
    private boolean checkFirstLogin(OAuthUserInfo oAuthUserInfo) {
        Optional<User> user = userPort.findUserByEmail(oAuthUserInfo.getEmail());
        return !user.isPresent();
    }

    // 유저 정보 가져오기
    private User loadUser(OAuthUserInfo oAuthUserInfo) {
        return userPort.findUserByEmail(oAuthUserInfo.getEmail())
                .orElse(null);
    }

    // 새롭게 생성
    private User newUser(OAuthUserInfo oAuthUserInfo) {
        // 생성시간 수정
        LocalDateTime now = LocalDateTime.now();

        // 이메일, 이름, 이미지 정보만 포함되어있는 유저 정보 저장하기
        User user = new User(oAuthUserInfo.getEmail(), oAuthUserInfo.getUserName(), oAuthUserInfo.getImageUrl(), now, now);

        return userPort.saveUser(user);
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

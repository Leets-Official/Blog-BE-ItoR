package com.blog.domain.login.service;

import com.blog.common.EncryptUtils;
import com.blog.common.response.ApiResponse;
import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.domain.login.api.dto.request.LoginEmailRequest;
import com.blog.domain.login.api.dto.response.LoginResponse;
import com.blog.domain.users.domain.Users;
import com.blog.domain.users.service.UsersService;
import com.blog.global.security.jwt.JwtUtil;
import com.blog.global.security.jwt.repository.TokenStore;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    private final JwtUtil jwtUtil;
    private final UsersService usersService;
    private final TokenStore tokenStore;

    public LoginService(JwtUtil jwtUtil, UsersService usersService, TokenStore tokenStore){
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
        this.tokenStore = tokenStore;
    }

    // 이메일 로그인
    public ApiResponse<LoginResponse> emailLogin(LoginEmailRequest request) {

        String hashedPassword = EncryptUtils.sha256(request.password());
        Users user = usersService.getUsersByEmailAndPassword(request, hashedPassword);

        if (user == null){

            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        LoginResponse response = createTokens(user, request.refreshToken());

        return ApiResponse.ok(response);
    }

    // 카카오 로그인
    public Users getUsersByName(String name)  {

        return usersService.getUsersByName(name);
    }


    public LoginResponse createTokens(Users user, String refreshToken) {
        String accessToken;
        String newRefreshToken;

        if (refreshToken != null && jwtUtil.validateToken(refreshToken) &&
                tokenStore.isValidStoredToken(user.getUserId(), refreshToken)) {

            accessToken = jwtUtil.createAccessToken(user);
            newRefreshToken = refreshToken;

        } else {
            accessToken = jwtUtil.createAccessToken(user);
            newRefreshToken = jwtUtil.createRefreshToken(user);

            tokenStore.storeRefreshToken(user.getUserId(), newRefreshToken);
        }

        return new LoginResponse(accessToken, newRefreshToken, user);
    }
}

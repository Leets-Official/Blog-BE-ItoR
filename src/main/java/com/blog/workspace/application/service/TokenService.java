package com.blog.workspace.application.service;

import com.blog.common.security.jwt.provider.JwtTokenProvider;
import com.blog.workspace.adapter.in.web.dto.response.UserLoginResponse;
import com.blog.workspace.application.in.token.TokenUseCase;
import com.blog.workspace.application.out.token.TokenPort;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.domain.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class TokenService implements TokenUseCase {

    /*
       쿠키 내의 리프레쉬 토큰을 바탕으로 accessToken 재발급 서비스
       - RefreshToken 존재, AccessToken 존재 : 로그인 그대로
       - RefreshToken 존재, AccessToken 없음 : "토큰 재발급 ! <- 해당 사항을 처리한다."
       - RefreshToken 없음 ,AccessToken 존재 : 토큰의 만료시간까지만 사용 가능, 추후 로그인 처음부터
       - RefreshToken 없음 ,AccessToken 없음 : 로그인 처음부터
     */

    private final TokenPort tokenPort;

    // 토큰 생성
    private final JwtTokenProvider jwtTokenProvider;
    private final UserPort userPort;

    //생성자
    public TokenService(TokenPort tokenPort, UserPort userPort, JwtTokenProvider jwtTokenProvider) {
        this.tokenPort = tokenPort;
        this.userPort = userPort;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserLoginResponse getAccessTokenByRefreshToken(HttpServletRequest httpServletRequest) {

        // 쿠키에서 refreshToken 가져오기
        String refreshToken = getRefreshTokenFromCookie(httpServletRequest);

        // Refresh 토큰 검증 체크, 예외처리는 내부에서 진행
        boolean validated = jwtTokenProvider.validateJwt(refreshToken);

        if (!validated) {
            throw new IllegalStateException("RefreshToken이 만료되었기에 다시 로그인 해야합니다.");
        } else {

            // 리프레쉬 토큰 예외처리
            Long userId = tokenPort.getUserByToken(refreshToken)
                    .orElseThrow(() -> new IllegalStateException("RefreshToken에 대한 유저가 없습니다."));

            // 유저 예외처리
            User user = userPort.findMe(userId)
                    .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

            // 토큰 재발급 처리
            String accessToken = jwtTokenProvider.createAccessToken(user);


            return new UserLoginResponse(user, accessToken, false);
        }

    }

    // 쿠키에서 refreshToken 추출하는 메서드
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies ==null){
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue(); // refreshToken 값을 반환
            }
        }

        return null; // 쿠키에 refreshToken이 없으면 null 반환
    }

}

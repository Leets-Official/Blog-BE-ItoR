package com.blog.user.application;

import com.blog.token.domain.Token;
import com.blog.token.domain.TokenRepository;
import com.blog.token.infrastructure.JwtTokenProvider;
import com.blog.token.presentation.dto.TokenResponse;
import com.blog.user.domain.User;
import com.blog.user.domain.UserRepository;
import com.blog.user.presentation.dto.KakaoLoginRequest;
import com.blog.user.presentation.dto.UserResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.sql.Timestamp;
import java.util.Map;

@Service
public class KakaoUserServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public KakaoUserServiceImpl(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public TokenResponse login(Object request) {
        KakaoLoginRequest kakaoRequest = (KakaoLoginRequest) request;
        String kakaoToken = kakaoRequest.getKakaoToken();

        // 1. 카카오 API에서 사용자 정보 조회 (지금은 MOCK)
        KakaoProfile profile = fetchKakaoProfile(kakaoToken);

        // 2. DB에서 이메일 기준으로 유저 검색
        User user = userRepository.findByEmail(profile.getEmail());
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // 3. 없으면 자동 회원가입
        if (user == null) {
            user = new User(
                    null,
                    profile.getEmail(),
                    null, // 비밀번호 없음
                    profile.getName(),
                    null,
                    null,
                    profile.getProfileImage(),
                    null,
                    "kakao",
                    now,
                    now,
                    true
            );
            userRepository.save(user);
        }

        // 4. JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);
        Timestamp expiresAt = jwtTokenProvider.getRefreshTokenExpiry();

        tokenRepository.save(new Token(user.getUserId(), refreshToken, expiresAt, now));

        // 5. 응답 생성
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setName(user.getName());
        userResponse.setProfileImage(user.getProfileImage());

        TokenResponse response = new TokenResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresAt(expiresAt.toString());
        response.setUser(userResponse);

        return response;
    }

    @Override
    public String getLoginType() {
        return "kakao";
    }

    private KakaoProfile fetchKakaoProfile(String kakaoAccessToken) {
        try {
            String kakaoUrl = "https://kapi.kakao.com/v2/user/me";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + kakaoAccessToken);
            headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(
                    kakaoUrl,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map body = response.getBody();
            if (body == null || !body.containsKey("kakao_account")) {
                throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
            }

            Map kakaoAccount = (Map) body.get("kakao_account");
            Map profile = (Map) kakaoAccount.get("profile");

            KakaoProfile result = new KakaoProfile();
            result.setEmail((String) kakaoAccount.get("email"));
            result.setName((String) profile.get("nickname"));
            result.setProfileImage((String) profile.get("profile_image_url"));

            return result;

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패", e);
        }
    }


    // 내부 DTO 형태
    static class KakaoProfile {
        private String email;
        private String name;
        private String profileImage;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getProfileImage() { return profileImage; }
        public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    }
}

package com.blog.global.auth.service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.auth.jwtUtil.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final RestTemplate restTemplate = new RestTemplate();
  private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
  private static final String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";
  private static final String CLIENT_ID = "YOUR_KAKAO_REST_API_KEY";
  private static final String REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public String login(String email, String password) {
    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (user.getPassword().equals(password)) {
        return JwtUtil.generateToken(user.getId().toString(), user.getEmail());
      }
      throw new RuntimeException("Invalid password");
    }
    throw new RuntimeException("User not found");
  }

  public String kakaoLogin(String code) {
    String accessToken = getKakaoAccessToken(code);
    Map<String, Object> kakaoUser = getKakaoUserInfo(accessToken);
    String email = (String) ((Map<String, Object>) kakaoUser.get("kakao_account")).get("email");

    Optional<User> existingUser = userRepository.findByEmail(email);
    User user = existingUser.orElseGet(() -> registerKakaoUser(email));

    return JwtUtil.generateToken(user.getId().toString(), user.getEmail());
  }

  private String getKakaoAccessToken(String code) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      String body = "grant_type=authorization_code" +
          "&client_id=" + CLIENT_ID +
          "&redirect_uri=" + REDIRECT_URI +
          "&code=" + code;

      HttpEntity<String> request = new HttpEntity<>(body, headers);

      ResponseEntity<String> response = restTemplate.exchange(
          KAKAO_TOKEN_URL, HttpMethod.POST, request, String.class
      );

      System.out.println("🔍 카카오 토큰 응답: " + response.getBody());

      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

      if (!responseMap.containsKey("access_token")) {
        throw new RuntimeException("카카오 액세스 토큰 요청 실패: " + response.getBody());
      }

      return (String) responseMap.get("access_token");
    } catch (Exception e) {
      throw new RuntimeException("카카오 액세스 토큰 요청 중 예외 발생", e);
    }
  }

  private Map<String, Object> getKakaoUserInfo(String accessToken) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + accessToken);
      HttpEntity<Void> request = new HttpEntity<>(headers);

      ResponseEntity<String> response = restTemplate.exchange(KAKAO_USER_URL, HttpMethod.GET, request, String.class);
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.getBody(), Map.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to retrieve Kakao user info", e);
    }
  }

  private User registerKakaoUser(String email) {
    User newUser = new User(UUID.randomUUID(), "KakaoUser", "kakao", email, null, null, null);
    userRepository.save(newUser);
    return newUser;
  }
}
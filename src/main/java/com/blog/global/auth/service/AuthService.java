package com.blog.global.auth.service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.auth.jwtUtil.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
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

  public ResponseEntity<String> signup(Map<String, String> request) {
    String email = request.get("email");
    String password = request.get("password");

    if (userRepository.findByEmail(email).isPresent()) {
      return ResponseEntity.badRequest().body("[회원가입 실패] 이미 존재하는 이메일입니다.");
    }

    String hashedPassword = hashPassword(password);
    User newUser = new User(UUID.randomUUID(), "NewUser", "email", email, hashedPassword, null, null);
    userRepository.save(newUser);

    return ResponseEntity.ok("[회원가입 성공] 계정이 생성되었습니다.");
  }

  public ResponseEntity<String> login(Map<String, String> request) {
    String email = request.get("email");
    String password = request.get("password");

    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
      return ResponseEntity.badRequest().body("[로그인 실패] 존재하지 않는 사용자입니다.");
    }

    User user = userOpt.get();
    if (!verifyPassword(password, user.getPassword())) {
      return ResponseEntity.badRequest().body("[로그인 실패] 비밀번호가 일치하지 않습니다.");
    }

    String token = JwtUtil.generateToken(user.getId().toString(), user.getEmail());
    return ResponseEntity.ok("[로그인 성공] JWT: " + token);
  }

  public ResponseEntity<String> kakaoLogin(String code) {
    String accessToken = getKakaoAccessToken(code);
    Map<String, Object> kakaoUser = getKakaoUserInfo(accessToken);

    String email = getKakaoUserEmail(kakaoUser);
    Optional<User> existingUser = userRepository.findByEmail(email);
    User user = existingUser.orElseGet(() -> registerKakaoUser(email));

    String token = JwtUtil.generateToken(user.getId().toString(), user.getEmail());
    return ResponseEntity.ok("[카카오 로그인 성공] JWT: " + token);
  }

  private String getKakaoAccessToken(String code) {
    ResponseEntity<String> response = createKakaoTokenRequest(code);
    return parseKakaoTokenResponse(response);
  }

  private ResponseEntity<String> createKakaoTokenRequest(String code) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      String body = "grant_type=authorization_code" +
          "&client_id=" + CLIENT_ID +
          "&redirect_uri=" + REDIRECT_URI +
          "&code=" + code;

      HttpEntity<String> request = new HttpEntity<>(body, headers);
      return restTemplate.exchange(KAKAO_TOKEN_URL, HttpMethod.POST, request, String.class);
    } catch (Exception e) {
      throw new RuntimeException("[카카오 로그인 오류] 액세스 토큰 요청 중 예외 발생", e);
    }
  }

  private String parseKakaoTokenResponse(ResponseEntity<String> response) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

      if (!responseMap.containsKey("access_token")) {
        throw new RuntimeException("[카카오 오류] 액세스 토큰 요청 실패: " + response.getBody());
      }

      return (String) responseMap.get("access_token");
    } catch (Exception e) {
      throw new RuntimeException("[카카오 로그인 오류] 액세스 토큰 응답 파싱 중 오류 발생", e);
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
      throw new RuntimeException("[카카오 로그인 오류] 사용자 정보 요청 실패", e);
    }
  }

  private String getKakaoUserEmail(Map<String, Object> kakaoUser) {
    return (String) ((Map<String, Object>) kakaoUser.get("kakao_account")).get("email");
  }

  private User registerKakaoUser(String email) {
    User newUser = new User(UUID.randomUUID(), "KakaoUser", "kakao", email, null, null, null);
    userRepository.save(newUser);
    return newUser;
  }

  private String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder();
      for (byte b : hashedBytes) {
        hexString.append(String.format("%02x", b));
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new RuntimeException("[해싱 오류] 비밀번호 해싱 중 오류 발생", e);
    }
  }

  private boolean verifyPassword(String rawPassword, String hashedPassword) {
    return hashPassword(rawPassword).equals(hashedPassword);
  }
}
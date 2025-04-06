package com.blog.global.auth.service;

import com.blog.domain.user.domain.User;
import com.blog.global.auth.jwtUtil.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@Service
public class AuthService {

  @Value("${kakao.token-url}")
  private String kakaoTokenUrl;

  @Value("${kakao.user-url}")
  private String kakaoUserUrl;

  @Value("${kakao.client-id}")
  private String clientId;

  @Value("${kakao.redirect-uri}")
  private String redirectUri;

  private final RestTemplate restTemplate = new RestTemplate();

  public ResponseEntity<String> kakaoLogin(String code) {
    String accessToken = getKakaoAccessToken(code);
    Map<String, Object> kakaoUser = getKakaoUserInfo(accessToken);
    String email = getKakaoUserEmail(kakaoUser);
    String token = JwtUtil.generateToken("dummy-id", email);
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
          "&client_id=" + clientId +
          "&redirect_uri=" + redirectUri +
          "&code=" + code;

      HttpEntity<String> request = new HttpEntity<>(body, headers);
      return restTemplate.exchange(kakaoTokenUrl, HttpMethod.POST, request, String.class);
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

      ResponseEntity<String> response = restTemplate.exchange(kakaoUserUrl, HttpMethod.GET, request, String.class);
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(response.getBody(), Map.class);
    } catch (Exception e) {
      throw new RuntimeException("[카카오 로그인 오류] 사용자 정보 요청 실패", e);
    }
  }

  private String getKakaoUserEmail(Map<String, Object> kakaoUser) {
    return (String) ((Map<String, Object>) kakaoUser.get("kakao_account")).get("email");
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

  public User getUserInfoFromToken(String authorizationHeader) {
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      throw new RuntimeException("[토큰 오류] 유효하지 않은 Authorization 헤더입니다.");
    }

    String token = authorizationHeader.replace("Bearer ", "");
    Map<String, Object> claims = JwtUtil.verifyToken(token);
    String email = (String) claims.get("email");

    return new User(null, "Unknown", "jwt", email, null, null, null);
  }
}

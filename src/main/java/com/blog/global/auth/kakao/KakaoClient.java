package com.blog.global.auth.kakao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.blog.global.auth.kakao.dto.KakaoTokenResponseDto;
import com.blog.global.auth.kakao.dto.KakaoUserInfoResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
//엑세스 토큰 요청 + 사용자 정보 요청

@Component
public class KakaoClient {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public KakaoTokenResponseDto getToken(String code, String clientId, String redirectUri, String tokenUri) throws Exception {
		URL url = new URL(tokenUri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Accept", "application/json");

		String encodedRedirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		String encodedCode = URLEncoder.encode(code, "UTF-8");

		String params = "grant_type=authorization_code"
			+ "&client_id=" + clientId
			+ "&redirect_uri=" + encodedRedirectUri
			+ "&code=" + encodedCode;

		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
			writer.write(params);
			writer.flush();
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return objectMapper.readValue(sb.toString(), KakaoTokenResponseDto.class);
		}
	}

	// 카카오로부터 사용자 정보를 받아오는 메서드
	public KakaoUserInfoResponseDto getUserInfo(String accessToken, String userInfoUri) throws Exception {
		// 사용자 정보 요청 URL 생성
		URL url = new URL(userInfoUri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// HTTP 메서드 GET
		conn.setRequestMethod("GET");
		// 요청 헤더 (카카오가 요구하는 인증 방식)
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);

		// 응답 코드가 200(OK)이 아닐 경우 예외 처리 (선택적으로 추가 가능)
		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("카카오에서 사용자 정보를 가져오는데 실패했습니다. 응답 코드: " + responseCode);
		}

		// 응답 읽기: BufferedReader로 응답 스트림을 읽어 StringBuilder에 누적
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			// 누적된 JSON 문자열을 KakaoUserInfoResponseDto 객체로 변환 후 반환
			return objectMapper.readValue(sb.toString(), KakaoUserInfoResponseDto.class);
		}

	}
}

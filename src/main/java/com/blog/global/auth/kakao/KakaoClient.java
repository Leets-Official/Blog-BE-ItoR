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
import com.blog.global.config.error.ErrorCode;
import com.blog.global.config.error.exception.CommonException;
import com.fasterxml.jackson.databind.ObjectMapper;
//엑세스 토큰 요청 + 사용자 정보 요청

@Component
public class KakaoClient {

	private final ObjectMapper objectMapper = new ObjectMapper();
// ====== 작업 현황 throws Exception 지우고  try-catch 문 만들고 있었ㅇ므 ㅇㅇ
	public KakaoTokenResponseDto getToken(String code, String clientId, String redirectUri, String tokenUri) {
		try {
			HttpURLConnection conn = createPostConnection(tokenUri);
			String params = buildParams(code, clientId, redirectUri);
			writeRequest(conn, params);
			String response = readResponse(conn);

			return objectMapper.readValue(response, KakaoTokenResponseDto.class);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.KAKAO_TOKEN_REQUEST_FAILED);
		}
	}


	// 카카오로부터 사용자 정보를 받아오는 메서드
	public KakaoUserInfoResponseDto getUserInfo(String accessToken, String userInfoUri)  {
		try {
			URL url = new URL(userInfoUri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new CommonException(ErrorCode.KAKAO_USERINFO_REQUEST_FAILED);
			}

			String response = readResponse(conn);
			return objectMapper.readValue(response, KakaoUserInfoResponseDto.class);

		} catch (Exception e) {
			throw new CommonException(ErrorCode.KAKAO_USERINFO_PARSE_FAILED);
		}

	}

	private HttpURLConnection createPostConnection(String tokenUri) throws Exception {
		URL url = new URL(tokenUri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Accept", "application/json");
		return conn;
	}

	private String buildParams(String code, String clientId, String redirectUri) throws Exception {
		String encodedRedirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		String encodedCode = URLEncoder.encode(code, "UTF-8");

		return "grant_type=authorization_code"
			+ "&client_id=" + clientId
			+ "&redirect_uri=" + encodedRedirectUri
			+ "&code=" + encodedCode;
	}

	private void writeRequest(HttpURLConnection conn, String params) throws Exception {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
			writer.write(params);
			writer.flush();
		}
	}

	private String readResponse(HttpURLConnection conn) throws Exception {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}
	}

}

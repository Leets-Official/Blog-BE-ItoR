package com.blog.controller;

import com.blog.global.auth.controller.AuthController;
import com.blog.global.auth.jwt.JwtUtil;
import com.blog.global.auth.service.AuthService;
import com.blog.global.auth.service.KakaoAuthService;
import com.blog.global.auth.dto.LoginResponseDto;
import com.blog.global.auth.dto.SignUpResponseDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthService authService;

	@MockBean
	private KakaoAuthService kakaoAuthService;

	@MockBean
	private JwtUtil jwtUtil;



	@Test
	@DisplayName("P1 - 로그인 API가 200 OK로 응답해야 함")
	void testLogin() throws Exception {
		// given
		LoginResponseDto mockResponse = new LoginResponseDto("access-token", "refresh-token");
		when(authService.login("test@example.com", "password123")).thenReturn(mockResponse);

		String requestJson = """
            {
              "email": "test@example.com",
              "password": "password123"
            }
        """;

		// when & then
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("성공"))
			.andExpect(jsonPath("$.result.accessToken").value("access-token"))
			.andExpect(jsonPath("$.result.refreshToken").value("refresh-token"));
	}

	@Test
	@DisplayName(" 회원가입 API가 200 OK로 응답해야 함")
	void testSignup() throws Exception {
		// given
		SignUpResponseDto mockResponse = new SignUpResponseDto("가입완료");
		when(authService.signUp(org.mockito.ArgumentMatchers.any())).thenReturn(mockResponse);

		String requestJson = """
    {
      "email": "test@example.com",
      "password": "password123!",
      "passwordConfirm": "password123!",
      "name": "문석준",
      "nickName": "zuny",
      "birthDate": "2000-01-01",
      "introduce": "안녕하세요! 저는 백엔드 개발자입니다.",
      "profileImageUrl": "https://example.com/profile.jpg"
    }
""";

		// when & then
		mockMvc.perform(post("/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("성공"))
			.andExpect(jsonPath("$.result.message").value("가입완료"));
	}

	@Test
	@DisplayName("토큰 재발급 API가 200 OK로 응답해야 함")
	void testReissue() throws Exception {
		// given
		LoginResponseDto mockResponse = new LoginResponseDto("new-access-token", "new-refresh-token");
		when(authService.reissueAccessToken("sample-refresh-token")).thenReturn(mockResponse);

		String requestJson = """
        {
          "refreshToken": "sample-refresh-token"
        }
    """;

		// when & then
		mockMvc.perform(post("/auth/reissue")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("성공"))
			.andExpect(jsonPath("$.result.accessToken").value("new-access-token"))
			.andExpect(jsonPath("$.result.refreshToken").value("new-refresh-token"));
	}

}

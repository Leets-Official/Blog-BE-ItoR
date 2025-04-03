package com.blog.global.auth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//카카오에서 반환해주는 속성하나하나 필드에 넣기 힘듬
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDto {
	@JsonProperty("id")
	private Long id;

	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	public String getEmail() {
		return kakaoAccount.email;
	}

	public String getNickname() {
		return kakaoAccount.profile.nickname;
	}

	// 중첩 클래스로 카카오 계정 정보 구조를 매핑
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoAccount {

		// 이메일 필드
		@JsonProperty("email")
		private String email;

		// 프로필 정보 (닉네임 등)
		@JsonProperty("profile")
		private Profile profile;

		public String getEmail() {
			return email;
		}

		public Profile getProfile() {
			return profile;
		}

		// 중첩 클래스: profile
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Profile {
			@JsonProperty("nickname")
			private String nickname;

			public String getNickname() {
				return nickname;
			}
		}
	}
}

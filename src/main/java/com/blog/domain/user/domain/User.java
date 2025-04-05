package com.blog.domain.user.domain;

import java.time.LocalDate;

import com.blog.domain.auth.jwt.encoder.PasswordEncoder;
import com.blog.domain.user.dto.UserSaveDTO;
import com.blog.global.common.entity.BaseEntity;

public class User extends BaseEntity {

	long id;
	String email;
	String password;
	String name;
	String nickname;
	LocalDate birthDate;
	String profileImageUrl;
	String introduction;

	String refreshToken;
	long kakaoId;

	public User(Long id, String email, String name) {
		this.id = id;
		this.email = email;
		this.name = name;
	}

	public User(long userId, String email, String password, String name) {
		this.id = userId;
		this.email = email;
		this.password = password;
		this.name = name;
	}

	public User(String email, String password, String name, long kakaoId) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.kakaoId = kakaoId;
	}


	public User (String email, String password, String name, String nickname,
		String introduction, LocalDate birthDate, String profileImageUrl, long kakaoId) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.kakaoId = kakaoId;
		this.nickname = nickname;
		this.introduction = introduction;
		this.birthDate = birthDate;
		this.profileImageUrl = profileImageUrl;
	}

	public static User fromDTO(String email, String password, String name, String nickname,
		String introduction, LocalDate birthDate, String profileImageUrl, long kakaoId) {

		return new User(email, password, name, nickname, introduction, birthDate, profileImageUrl, kakaoId);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getNickname() {
		return nickname;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getIntroduction() {
		return introduction;
	}

	public String getRefreshToken() {
		return refreshToken;
	}


	public long getKakaoId(){ return kakaoId; }

}

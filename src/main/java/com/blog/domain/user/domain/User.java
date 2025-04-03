package com.blog.domain.user.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

	private int userId;
	private String name;
	private String nickName;
	private String email;
	private String password;
	private UserType userType;
	private LocalDate birthDate;
	private String introduce;
	private String profileImageUrl;
	private LocalDateTime createdAt;

	//Row Mapper 전용 생성자
	public User ( int userId,String name,
		String nickName,
		String email,
		String password,
		UserType userType,
		LocalDate birthDate,
		String introduce,
		String profileImageUrl,
		LocalDateTime createdAt) {
		this.userId = userId;
		this.name = name;
		this.nickName = nickName;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.birthDate = birthDate;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
		this.createdAt = createdAt;
	}

	//생성자 오버로딩
	public User(String name, String nickName, String email, String password,
		UserType userType, LocalDate birthDate, String introduce,
		String profileImageUrl, LocalDateTime createdAt) {
		this.name = name;
		this.nickName = nickName;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.birthDate = birthDate;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
		this.createdAt = createdAt;
	}


	public static User createEmailUser(
		String name,
		String nickName,
		String email,
		String password,
		LocalDate birthDate,
		String introduce,
		String profileImageUrl
	) {
		return new User(
			name,
			nickName,
			email,
			password,
			UserType.LOCAL,
			birthDate,
			introduce,
			profileImageUrl,
			LocalDateTime.now()
		);
	}

	public static User createKakaoUser(String nickname, String email) {
		return new User(
			0,
			nickname,
			nickname,
			email,
			null,
			UserType.KAKAO,
			null,
			null,
			null,
			LocalDateTime.now()
		);
	}


	public int getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public UserType getUserType() {
		return userType;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getIntroduce() {
		return introduce;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

}

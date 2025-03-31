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

	private User ( String name,
		String nickName,
		String email,
		String password,
		UserType userType,
		LocalDate birthDate,
		String introduce,
		String profileImageUrl,
		LocalDateTime createdAt) {
		this.userId = 0;
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

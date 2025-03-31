package com.blog.global.auth.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

public class SignUpRequestDto {

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "올바른 이메일 형식이어야 합니다.")
	private String email;

	@NotBlank(message = "비밀번호 입력은 필수입니다.")
	private String password;

	@NotBlank(message = "비밀번호가  일치하지 않습니다.") //커스텀 어노테이션 적용 고려중
	private String passwordConfirm;

	@NotBlank(message = " 이름은 필수입니다.")
	private String name;

	@NotBlank(message = "닉네임 입력은 필수입니다.")
	@Max(value = 20 , message = "닉네임은 최대 20자까지 입력 가능합니다.")
	private String nickName;

	@Past
	private LocalDate birthDate;

	@Max(value = 30 , message = "한 줄 소개는 최대 30자까지 입력 가능합니다.")
	private String introduce;

	private String profileImageUrl;

	public SignUpRequestDto() {
	}

	public SignUpRequestDto(String email, String password, String passwordConfirm, String name, String nickName,
		LocalDate birthDate, String introduce, String profileImageUrl) {
		this.email = email;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.name = name;
		this.nickName = nickName;
		this.birthDate = birthDate;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}


	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
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
}


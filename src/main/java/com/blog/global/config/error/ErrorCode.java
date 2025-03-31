package com.blog.global.config.error;

public enum ErrorCode {

	USER_NOT_FOUND(400, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
	INVALID_PASSWORD(401, "INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),
	PASSWORD_MISMATCH(400, "PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다."),
	EMAIL_ALREADY_EXISTS(400, "EMAIL_ALREADY_EXISTS", "이미 사용 중인 이메일입니다.");

	private final int status;
	private final String code;
	private final String message;

	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}

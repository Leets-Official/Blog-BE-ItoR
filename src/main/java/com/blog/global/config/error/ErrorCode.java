package com.blog.global.config.error;

public enum ErrorCode {

	//유저 관련
	USER_NOT_FOUND(400, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
	INVALID_PASSWORD(401, "INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),
	PASSWORD_MISMATCH(400, "PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다."),
	EMAIL_ALREADY_EXISTS(400, "EMAIL_ALREADY_EXISTS", "이미 사용 중인 이메일입니다."),

	//Access & Refresh Token 관련
	INVALID_TOKEN(401, "INVALID_TOKEN", "토큰이 유효하지 않습니다."),
	REFRESH_TOKEN_NOT_FOUND(404, "REFRESH_TOKEN_NOT_FOUND", "리프레쉬 토큰을 찾을 수 없습니다."),

	//카카오 관련
	KAKAO_TOKEN_REQUEST_FAILED(500, "TOKEN_REQUEST_FAILED", "카카오 토큰 요청에 실패했습니다."),
	KAKAO_TOKEN_PARSE_FAILED(500, "TOKEN_PARSE_FAILED", "카카오 토큰 응답 파싱에 실패했습니다."),
	KAKAO_USERINFO_REQUEST_FAILED(500, "USERINFO_REQUEST_FAILED", "카카오 사용자 정보 요청에 실패했습니다."),
	KAKAO_USERINFO_PARSE_FAILED(500, "USERINFO_PARSE_FAILED", "카카오 사용자 정보 파싱에 실패했습니다."),

	//post관련
	POST_NOT_FOUND(404, "POST_NOT_FOUND", "게시글을 찾을 수 없습니다"),
	POST_CREATE_FAILED(500,"POST_CREATE_FAILED", "게시글 생성에 실패했습니다."),
	POST_UPDATE_FAILED(500,"POST_UPDATE_FAILED","게시글 수정에 실패했습니다"),
	POST_DELETE_FAILED(500,"POST_DELETED_FAILED", "게시글 삭제에 실패했습니다"),
	POST_ALREADY_DELETED(400, "POST_ALREADY_DELETED", "이미 삭제된 게시글입니다."),
	FORBIDDEN_POST_ACCESS(403, "FORBIDDEN_POST_ACCESS", "해당 게시글에 대한 권한이 없습니다."),

	//파싱관련
	SERIALIZE_FAILED(500, "SERIALIZE_FAILED", "직렬화에 오류가 발생했습니다.");

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

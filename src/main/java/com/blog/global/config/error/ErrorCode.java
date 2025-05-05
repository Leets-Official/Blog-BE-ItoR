package com.blog.global.config.error;

public enum ErrorCode {

	//유저 관련
	USER_NOT_FOUND(400, "c40001", "사용자를 찾을 수 없습니다."),
	INVALID_PASSWORD(401, "c40101", "비밀번호가 일치하지 않습니다."),
	PASSWORD_MISMATCH(400, "c40002", "비밀번호가 일치하지 않습니다."),
	EMAIL_ALREADY_EXISTS(400, "c40003", "이미 사용 중인 이메일입니다."),

	//Access & Refresh Token 관련
	INVALID_TOKEN(401, "c40102", "토큰이 유효하지 않습니다."),
	REFRESH_TOKEN_NOT_FOUND(404, "c40401", "리프레쉬 토큰을 찾을 수 없습니다."),

	//카카오 관련
	KAKAO_TOKEN_REQUEST_FAILED(500, "c50001", "카카오 토큰 요청에 실패했습니다."),
	KAKAO_TOKEN_PARSE_FAILED(500, "c50002", "카카오 토큰 응답 파싱에 실패했습니다."),
	KAKAO_USERINFO_REQUEST_FAILED(500, "c50003", "카카오 사용자 정보 요청에 실패했습니다."),
	KAKAO_USERINFO_PARSE_FAILED(500, "c50004", "카카오 사용자 정보 파싱에 실패했습니다."),

	//post관련
	POST_NOT_FOUND(404, "c40402", "게시글을 찾을 수 없습니다"),
	POST_CREATE_FAILED(500,"c50005", "게시글 생성에 실패했습니다."),
	POST_UPDATE_FAILED(500,"c50006","게시글 수정에 실패했습니다"),
	POST_DELETE_FAILED(500,"c50007", "게시글 삭제에 실패했습니다"),
	POST_ALREADY_DELETED(400, "c40004", "이미 삭제된 게시글입니다."),
	FORBIDDEN_POST_ACCESS(403, "c40301", "해당 게시글에 대한 권한이 없습니다."),

	//comments 관련
	COMMENT_NOT_FOUND(404, "c40403", "댓글을 찾을 수 없습니다."),
	UNAUTHORIZED_COMMENT_ACCESS(403, "c40302", "댓글에 관한 권한이 없습니다."),

	//파싱관련
	SERIALIZE_FAILED(500, "c50008", "직렬화에 오류가 발생했습니다.");

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

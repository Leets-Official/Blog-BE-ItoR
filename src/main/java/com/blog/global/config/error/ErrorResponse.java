package com.blog.global.config.error;

public class ErrorResponse {

	private final int status;
	private final String message;
	private final String code;

	public ErrorResponse(ErrorCode errorCode) {
		this.status = errorCode.getStatus();
		this.message = errorCode.getMessage();
		this.code = errorCode.getCode();
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}

}

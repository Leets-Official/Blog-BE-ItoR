package com.blog.global.common.dto;

import java.time.LocalDateTime;

import com.blog.global.config.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

public class GlobalResponseDto<T> {

	private boolean isSuccess;
	private int status;
	private LocalDateTime timestamp;
	private String message;
	private String errorCode;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;

	public GlobalResponseDto() {}

	public GlobalResponseDto(boolean isSuccess, int status, String message, String errorCode, T result) {
		this.isSuccess = isSuccess;
		this.status = status;
		this.timestamp = LocalDateTime.now();
		this.message = message;
		this.errorCode = errorCode;
		this.result = result;
	}


	public static <T> GlobalResponseDto<T> success(T result) {
		return new GlobalResponseDto<>(true, 200, "성공", null, result);
	}

	public static <T> GlobalResponseDto<T> success() {
		return new GlobalResponseDto<>(true, 200, "성공", null, null);
	}

	public static <T> GlobalResponseDto<T> fail(ErrorCode errorCode) {
		return new GlobalResponseDto<>(false,
			errorCode.getStatus(),
			errorCode.getMessage(),
			errorCode.getCode(),
			null);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public int getStatus() {
		return status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public T getResult() {
		return result;
	}
}
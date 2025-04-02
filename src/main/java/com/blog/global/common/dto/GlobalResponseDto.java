package com.blog.global.common.dto;

import java.time.LocalDateTime;

import com.blog.global.config.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

public class GlobalResponseDto<T> {

	private boolean isSuccess;
	private int code;
	private LocalDateTime timestamp;
	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;

	public GlobalResponseDto() {}

	public GlobalResponseDto(boolean isSuccess, int code, String message, T result) {
		this.isSuccess = isSuccess;
		this.code = code;
		this.timestamp = LocalDateTime.now();
		this.message = message;
		this.result = result;
	}

	public static <T> GlobalResponseDto<T> success(T result) {
		return new GlobalResponseDto<>(true, 200, "성공", result);
	}

	public static <T> GlobalResponseDto<T> success() {
		return new GlobalResponseDto<>(true, 200, "성공", null);
	}

	public static <T> GlobalResponseDto<T> fail(ErrorCode errorCode) {
		return new GlobalResponseDto<>(false, errorCode.getStatus(), errorCode.getMessage(), null);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public int getCode() {
		return code;
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
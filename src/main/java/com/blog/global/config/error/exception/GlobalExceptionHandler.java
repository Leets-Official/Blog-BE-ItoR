package com.blog.global.config.error.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.global.common.dto.GlobalResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CommonException.class)
	public ResponseEntity<GlobalResponseDto<?>> handleCommonException(CommonException e) {
		return ResponseEntity
			.status(e.getErrorCode().getStatus())
			.body(GlobalResponseDto.fail(e.getErrorCode()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GlobalResponseDto<?>> handleUnexpected(Exception e) {
		return ResponseEntity
			.status(500)
			.body(new GlobalResponseDto<>(false, 500, "서버 내부 오류가 발생했습니다.", "INTERNAL_SERVER_ERROR", null));
	}

}

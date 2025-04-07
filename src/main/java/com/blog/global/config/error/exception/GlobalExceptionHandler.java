package com.blog.global.config.error.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GlobalResponseDto<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		// 필드 에러 메시지를 하나의 문자열로 결합
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getDefaultMessage())
			.collect(Collectors.joining(", "));
		return ResponseEntity.badRequest().body(GlobalResponseDto.error(errorMessage));
	}

}

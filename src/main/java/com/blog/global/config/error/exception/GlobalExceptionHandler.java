package com.blog.global.config.error.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.global.common.dto.GlobalResponseDto;
import com.blog.global.config.error.ErrorCode;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

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

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<GlobalResponseDto<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		// ex.getCause() 로 내부에 InvalidFormatException이 있을 수 있음
		String errorMessage = "입력 데이터 형식이 올바르지 않습니다.";
		if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
			errorMessage = "입력된 값 중 잘못된 형식이 있습니다: " +
				((com.fasterxml.jackson.databind.exc.InvalidFormatException) ex.getCause()).getValue();
		}
		return ResponseEntity.badRequest().body(GlobalResponseDto.error(errorMessage));
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<GlobalResponseDto<?>> handleMissingHeader(MissingRequestHeaderException ex) {
		return ResponseEntity
			.status(ErrorCode.MISSING_AUTH_HEADER.getStatus())
			.body(GlobalResponseDto.fail(ErrorCode.MISSING_AUTH_HEADER));
	}


}

package com.blog.global.config.error.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CommonException.class)
	public ResponseEntity<String> handleCommonException(CommonException e) {
		return ResponseEntity
			.status(400)
			.body(e.getErrorCode().getMessage());
	}
}

package com.blog.global.exception;


import com.blog.global.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

//@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ApiResponse<ExceptionDto> handleException(Exception e) {

        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<ExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        // 검증 에러 메시지 출력
        String errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();

        log.error(" 에러 로그 :{}", errorMsg);

        CustomException exception = new CustomException(ErrorCode.BAD_REQUEST);

        return ApiResponse.fail(exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<ExceptionDto> handleNoSuchElementException(NoSuchElementException e) {

        CustomException exception = new CustomException(ErrorCode.NOT_FOUND_END_POINT);

        return ApiResponse.fail(exception);
    }

    @ExceptionHandler(CustomException.class)
    public ApiResponse<ExceptionDto> handleCustomException(CustomException e) {

        return ApiResponse.fail(new CustomException(e.getErrorCode()));
    }
}


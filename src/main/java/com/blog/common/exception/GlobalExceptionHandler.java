package com.blog.common.exception;

import com.blog.common.response.ApiResponse;
import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.common.response.ExceptionDto;
import com.blog.workspace.application.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /// 공통 처리 메서드
    private ApiResponse<ExceptionDto> handleCustomException(CustomException customException, Exception e) {
        log.error("에러 로그 : {}", e.getMessage(), e);
        return ApiResponse.fail(customException);
    }

    /// 예외 처리
    // 최하위 예외처리
    @ExceptionHandler(Exception.class)
    public ApiResponse<ExceptionDto> handleException(Exception e) {

        // 예외 클래스 생성
        CustomException exception = new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, null);
        return handleCustomException(exception, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<ExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        // 검증 에러 메시지 출력
        String errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();

        CustomException exception = new CustomException(ErrorCode.BAD_REQUEST, errorMsg);
        return handleCustomException(exception, e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<ExceptionDto> handleNoSuchElementException(NoSuchElementException e) {

        CustomException exception = new CustomException(ErrorCode.NOT_FOUND_END_POINT, null);
        return handleCustomException(exception, e);
    }

    /// 특정 에러 처리
    // 유저
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            DuplicationUserException.class,
            NotSamePasswordException.class,
            NotEqualLoginPassword.class,
            NotEmailException.class
    })
    public ApiResponse<ExceptionDto> handleUserExceptions(Exception e) {

        CustomException exception = null;

        if (e instanceof DuplicationUserException) {
            exception = new CustomException(ErrorCode.USER_BAD_REQUEST, null);
        } else if (e instanceof NotSamePasswordException) {
            exception = new CustomException(ErrorCode.USER_PASSWORD_BAD_REQUEST, null);
        } else if (e instanceof NotEqualLoginPassword) {
            exception = new CustomException(ErrorCode.USER_PASSWORD_BAD_REQUEST, null);
        } else if (e instanceof NotEmailException) {
            exception = new CustomException(ErrorCode.NO_EMAIL, null);
        }

        return handleCustomException(exception, e);
    }

    // 게시글, 댓글 관련 예외 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            NotEqualUpdateException.class,
            NotEqualDeleteException.class,
            NotRequestException.class
    })
    public ApiResponse<ExceptionDto> handlePostExceptions(RuntimeException e) {
        CustomException exception = null;

        if (e instanceof NotEqualUpdateException) {
            exception = new CustomException(ErrorCode.NO_UPDATE, null);
        } else if (e instanceof NotEqualDeleteException) {
            exception = new CustomException(ErrorCode.NO_DELETE, null);
        } else if (e instanceof NotRequestException) {
            exception = new CustomException(ErrorCode.NO_UPDATE_PARAM, null);
        }

        return handleCustomException(exception, e);
    }

}

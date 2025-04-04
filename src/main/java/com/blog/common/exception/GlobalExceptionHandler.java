package com.blog.common.exception;

import com.blog.common.response.ApiResponse;
import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.common.response.ExceptionDto;
import com.blog.workspace.application.service.exception.DuplicationUserException;
import com.blog.workspace.application.service.exception.NotEmailException;
import com.blog.workspace.application.service.exception.NotEqualLoginPassword;
import com.blog.workspace.application.service.exception.NotSamePasswordException;
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

    @ExceptionHandler(Exception.class)
    public ApiResponse<ExceptionDto> handleException(Exception e) {

        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, null));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<ExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        // 검증 에러 메시지 출력
        String errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();

        log.error(" 에러 로그 :{}", errorMsg);

        CustomException exception = new CustomException(ErrorCode.BAD_REQUEST, errorMsg);

        return ApiResponse.fail(exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<ExceptionDto> handleNoSuchElementException(NoSuchElementException e) {

        CustomException exception = new CustomException(ErrorCode.NOT_FOUND_END_POINT, null);

        return ApiResponse.fail(exception);
    }

    /// 회원가입 관련 에러 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicationUserException.class)
    public ApiResponse<ExceptionDto> handleException(DuplicationUserException e) {

        return ApiResponse.fail(new CustomException(ErrorCode.USER_BAD_REQUEST, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotSamePasswordException.class)
    public ApiResponse<ExceptionDto> handlePasswordException(NotSamePasswordException e) {

        return ApiResponse.fail(new CustomException(ErrorCode.USER_PASSWORD_BAD_REQUEST, e.getMessage()));
    }

    /// 로그인 관련 에러처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotEqualLoginPassword.class)
    public ApiResponse<ExceptionDto> handlePasswordException(NotEqualLoginPassword e) {

        return ApiResponse.fail(new CustomException(ErrorCode.USER_PASSWORD_BAD_REQUEST, null));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotEmailException.class)
    public ApiResponse<ExceptionDto> handlePasswordException(NotEmailException e) {

        return ApiResponse.fail(new CustomException(ErrorCode.NO_EMAIL, null));
    }

}

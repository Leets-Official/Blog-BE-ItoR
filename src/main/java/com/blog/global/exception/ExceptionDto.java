package com.blog.global.exception;

public class ExceptionDto {

    private final Integer code;
    private final String message;

    // constructor
    public ExceptionDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }


    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode);
    }
}

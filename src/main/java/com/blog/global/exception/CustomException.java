package com.blog.global.exception;


public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }



    public String getMessage() {
        return errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

package com.blog.common.response;

public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    private final String errorMsg;

    public CustomException(ErrorCode errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /// @Getter
    public String getMessage() {
        return errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}

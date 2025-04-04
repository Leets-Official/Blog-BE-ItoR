package com.blog.common.response;

import com.blog.common.response.ErrorCode;

public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException (ErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }

}

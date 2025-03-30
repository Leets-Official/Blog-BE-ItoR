package com.blog.global.config.error.exception;

import com.blog.global.config.error.ErrorCode;

public class CommonException extends RuntimeException {

  private final ErrorCode errorCode;

  public CommonException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}

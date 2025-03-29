package com.blog.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({"code", "message", "errorMsg"})
public class ExceptionDto {

    @NotNull
    private final Integer code;

    @NotNull
    private final String message;

    private final String errorMsg;

    public ExceptionDto(ErrorCode errorCode, String errorMsg) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errorMsg = errorMsg;
    }

    public static ExceptionDto of(ErrorCode errorCode, String errorMsg) {
        return new ExceptionDto(errorCode, errorMsg);
    }

    /// @Getter
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}

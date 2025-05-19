package com.blog.common.response;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

public record ExceptionDto(
        @NotNull HttpStatus httpStatus,
        @NotNull String message
) {
    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(
                errorCode.getHttpStatus(),
                errorCode.getMessage()
        );
    }
}

package com.blog.common.response;

import jakarta.validation.constraints.NotNull;

public record ExceptionDto(
        @NotNull String message
) {
    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(
                errorCode.getMessage()
        );
    }
}

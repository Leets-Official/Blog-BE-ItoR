package com.blog.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Test Error
    TEST_ERROR(100, HttpStatus.BAD_REQUEST, "테스트 에러입니다."),
    // 400 Bad Request
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // 403 Bad Request
    Forbidden(403, HttpStatus.FORBIDDEN, "접속 권한이 없습니다."),
    // 404 Not Found
    NOT_FOUND_END_POINT(404, HttpStatus.NOT_FOUND, "요청한 대상이 존재하지 않습니다."),
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private Integer code;
    private HttpStatus httpStatus;
    private String message;

    // Constructor
    ErrorCode(int i, HttpStatus httpStatus, String s) {
        this.code = i;
        this.httpStatus = httpStatus;
        this.message = s;
    }

    // Getter
    public Integer getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

}
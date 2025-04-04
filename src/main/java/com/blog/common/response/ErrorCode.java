package com.blog.common.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 404 Not Found
    NOT_FOUND_END_POINT(404, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    // 403 Forbidden
    Forbidden(403, HttpStatus.FORBIDDEN, "접속 권한이 없습니다."),
    // 400 Bad Request
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATE_NICKNAME(400, HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."),
    DUPLICATE_EMAIL(400, HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    // 회원 관리 에러

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(Integer code, HttpStatus httpStatus, String message){
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

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

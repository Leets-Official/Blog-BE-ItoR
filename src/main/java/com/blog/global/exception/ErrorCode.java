package com.blog.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

     // Test Error
    TEST_ERROR(100, HttpStatus.BAD_REQUEST, "테스트 에러입니다."),
    // 400 Bad Request
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // 403 Forbidden
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "접속 권한이 없습니다."),
    // 404 Not Found
    NOT_FOUND_END_POINT(404, HttpStatus.NOT_FOUND, "요청한 대상이 존재하지 않습니다."),
    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // Custom Error (추후에 프론트랑 맞출 수 있음)
    DUPLICATE_EMAIL(1001, HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    USER_NOT_FOUND(1002, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_CREDENTIALS(1003, HttpStatus.UNAUTHORIZED, "인증 정보가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(1004, HttpStatus.UNAUTHORIZED, "재발급 토큰이 올바르지 않습니다."),
    INVALID_ACCESS_TOKEN(1005, HttpStatus.UNAUTHORIZED, "접근 토큰이 올바르지 않습니다."),
    INVALID_TOKEN(1006, HttpStatus.UNAUTHORIZED, "토큰이 생성되지 않았습니다"),
    USER_NOT_FOUND_IN_COOKIE(1007, HttpStatus.NOT_FOUND,"쿠키에서 사용자정보를 찾을 수 없습니다."),

    POST_NOT_FOUND(2001, HttpStatus.NOT_FOUND, "요청한 게시글을 찾을 수 없습니다."),
    UNAUTHORIZED_POST_ACCESS(2002, HttpStatus.FORBIDDEN, "해당 게시글에 접근할 권한이 없습니다."),
    POST_TYPE_NOT_FOUND(2003, HttpStatus.NOT_FOUND, "게시글 타입을 찾을 수 없습니다."),


    COMMENT_NOT_FOUND(3001, HttpStatus.NOT_FOUND, "요청한 댓글을 찾을 수 없습니다."),
    INVALID_FILE_FORMAT(4001, HttpStatus.BAD_REQUEST, "업로드된 파일 형식이 올바르지 않습니다."),
    INVALID_INPUT(4002, HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

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

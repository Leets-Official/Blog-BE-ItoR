package com.blog.common.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Test Error
    TEST_ERROR(100, HttpStatus.BAD_REQUEST, "테스트 에러입니다."),

    // 400 User Request
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 403 Bad Reques
    Forbidden(403, HttpStatus.FORBIDDEN, "접속 권한이 없습니다."),

    // 404 Not Found
    NOT_FOUND_END_POINT(404, HttpStatus.NOT_FOUND, "요청한 대상이 존재하지 않습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    /// 회원 가입 관련 에러 처리
    USER_BAD_REQUEST(1001, HttpStatus.BAD_REQUEST, "사용중인 이메일입니다"),
    USER_PASSWORD_BAD_REQUEST(1002, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),

    /// 로그인 관련 에러 처리
    NO_EMAIL(1010, HttpStatus.BAD_REQUEST, "가입되지 않은 이메일 입니다"),

    /// 게시글 관련 에러처리
    NO_UPDATE(1020, HttpStatus.BAD_REQUEST, "글 작성자가 아니기에 게시글을 수정할 수 없습니다."),
    NO_DELETE(1021,HttpStatus.BAD_REQUEST,"게시글 작성자와 삭제 요청자가 서로 다릅니다."),
    NO_UPDATE_PARAM(1022, HttpStatus.BAD_REQUEST, "제목, 내용 중 하나는 수정해야합니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(Integer code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    /// @Getter
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

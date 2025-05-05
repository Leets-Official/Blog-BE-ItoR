package com.blog.common.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    // 로그아웃
    LOGOUT_SUCCESS(200, HttpStatus.OK, "로그아웃 성공했습니다."),

    // 게시글
    POST_CREATE_SUCCESS(201, HttpStatus.CREATED, "게시글이 작성 되었습니다."),
    POST_DELETE_SUCCESS(200, HttpStatus.OK, "게시글 삭제 성공했습니다."),
    POST_UPDATE_SUCCESS(200, HttpStatus.OK, "게시글 수정 성공 했습니다."),

    // 댓글
    COMMENT_CREATE_SUCCESS(201, HttpStatus.CREATED, "댓글이 작성되었습니다."),
    COMMENT_DELETE_SUCCESS(200, HttpStatus.OK, "댓글 삭제 성공했습니다."),
    COMMENT_UPDATE_SUCCESS(200, HttpStatus.OK, "댓글 수정 성공했습니다.");
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    ResponseCode(Integer code, HttpStatus httpStatus, String message){
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

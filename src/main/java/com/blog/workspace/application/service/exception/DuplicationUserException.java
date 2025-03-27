package com.blog.workspace.application.service.exception;

public class DuplicationUserException extends RuntimeException {

    private final String message;

    public DuplicationUserException(String message) {
        this.message = message;
    }
}

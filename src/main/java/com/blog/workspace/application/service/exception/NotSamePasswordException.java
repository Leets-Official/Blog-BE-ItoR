package com.blog.workspace.application.service.exception;

public class NotSamePasswordException extends RuntimeException {

    public NotSamePasswordException(String message) {
        super(message);
    }
}

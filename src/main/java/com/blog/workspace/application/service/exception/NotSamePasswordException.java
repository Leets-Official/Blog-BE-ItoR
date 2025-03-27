package com.blog.workspace.application.service.exception;

public class NotSamePasswordException extends RuntimeException {
    private final String message;

    public NotSamePasswordException(String message) {
        this.message = message;
    }
}

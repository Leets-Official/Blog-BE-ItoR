package com.blog.workspace.application.service.exception;

public class NotEqualLoginPassword extends RuntimeException {

    public NotEqualLoginPassword(String message) {
        super(message);

    }
}

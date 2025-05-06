package com.blog.comment.exception;

public class CommentException extends RuntimeException{


    //메세지만 전달
    public CommentException(String message) {
        super(message);
    }

    //메세지+원인 예외 전달
    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }








}//class

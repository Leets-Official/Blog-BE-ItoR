package com.blog.comment.service;

import com.blog.comment.exception.CommentException;
import com.blog.comment.record.CommentRecord;

import java.util.List;

public interface CommentService {

    List<CommentRecord> getComments(int postId);
    int    createComment(CommentRecord record);
    void   updateComment(CommentRecord record);
    void   deleteComment(CommentRecord record);














}//interface

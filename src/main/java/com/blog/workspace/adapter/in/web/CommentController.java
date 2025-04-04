package com.blog.workspace.adapter.in.web;

import com.blog.workspace.application.in.comment.CommentUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentUseCase commentService;

    public CommentController(CommentUseCase commentService) {
        this.commentService = commentService;
    }
}

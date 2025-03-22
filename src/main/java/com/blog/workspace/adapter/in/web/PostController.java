package com.blog.workspace.adapter.in.web;

import com.blog.workspace.application.in.PostUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostUseCase postService;

    public PostController(PostUseCase postService) {
        this.postService = postService;
    }
}

package com.blog.workspace.application.domain;

import com.blog.workspace.domain.post.Post;

import java.time.LocalDateTime;

public class PostFixtures {

    public static Post stub(Long postId) {
        return Post.fromDB(postId, 1L, "테스트", LocalDateTime.now(), LocalDateTime.now());
    }

}

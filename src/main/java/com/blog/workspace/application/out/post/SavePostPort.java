package com.blog.workspace.application.out.post;

import com.blog.workspace.domain.post.Post;

public interface SavePostPort {

    /// 저장하기

    Post savePost(Post post);

    Post updatePost(Post post);
}

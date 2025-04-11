package com.blog.workspace.application.out.post;

import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import com.blog.workspace.domain.post.Post;

import java.util.*;

public interface LoadPostPort {

    // 상세 조회
    Optional<Post> loadPost(Long postId);

    // 목록 조회, 페이징네이션 필요
    Page<Post> loadPosts(Pageable pageable, Long userId);

    // 게시글 작성자 여부 확인
    boolean checkPostByUserId(Long userId, Long postId);


}

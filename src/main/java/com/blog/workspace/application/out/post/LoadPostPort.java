package com.blog.workspace.application.out.post;

import com.blog.workspace.domain.post.Post;

import java.util.*;

public interface LoadPostPort {

    // 상세 조회
    Optional<Post> loadPost(Long postId);

    /// 목록 조회, 추후 페이징네이션 필요
    /// Pageable 및 Page 대체 기능 필요

    // 목록 조회
    List<Post> loadPosts();

    // 게시글 작성자 여부 확인
    boolean checkPostByUserId(Long userId, Long postId);


}

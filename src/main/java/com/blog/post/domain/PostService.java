package com.blog.post.domain;

import com.blog.post.presentation.dto.PostRequest;
import com.blog.post.presentation.dto.PostResponse;

import java.util.List;

public interface PostService {

    // 게시물 등록
    PostResponse createPost(PostRequest request, int userId);

    // 게시물 조회
    List<PostResponse> getAllPosts();
    PostResponse getPostById(int postId);

    // 게시물 수정
    PostResponse updatePost(int postId, PostRequest request, int userId);

    // 게시물 삭제
    void deletePost(int postId, int userId);
}

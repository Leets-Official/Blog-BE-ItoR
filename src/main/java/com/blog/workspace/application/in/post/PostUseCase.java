package com.blog.workspace.application.in.post;

import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import com.blog.workspace.adapter.in.web.dto.request.PostRequest;
import com.blog.workspace.adapter.in.web.dto.request.PostUpdateRequest;
import com.blog.workspace.adapter.in.web.dto.response.PostDetailResponse;
import com.blog.workspace.adapter.in.web.dto.response.PostListResponse;
import com.blog.workspace.domain.post.Post;

public interface PostUseCase {

    /*
        사용자는 로그인을 하지 않고도 게시물을 조회할 수 있어야 합니다.
        사용자는 로그인을 진행해야 게시물을 작성할 수 있어야 합니다.
        사용자는 자신의 게시물만 수정, 삭제할 수 있어야 합니다.
        게시물은 페이지네이션이 가능해야 합니다.
        페이지네이션은 클라이언트에서 입력 받을 수 있게 구현해야 합니다.
        게시물의 목록 조회와 게시물 내용을 보는 API는 별도로 구현되어야 합니다.
        게시물 조회시 댓글도 모두 조회할 수 있어야 합니다.
     */

    /// 게시글 작성
    Post savePost(PostRequest request, Long userId);

    /// 게시글 조회
    // 게시글 상세 조회
    PostDetailResponse loadPostById(Long id);

    // 나의 게시글 목록 조회(페이징 처리10개)
    Page<PostListResponse> loadPosts(Pageable pageable, Long userId);

    /// 게시글 수정
    Post updatePost(Long postId, Long userId, PostUpdateRequest request);

    /// 게시글 삭제
    void deletePost(Long userId, Long postId);




}

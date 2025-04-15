package com.blog.domain.posts.service;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.domain.comments.api.dto.response.CommentsResponse;
import com.blog.domain.comments.service.CommentsService;
import com.blog.domain.posts.api.dto.request.PostsRequest;
import com.blog.domain.posts.api.dto.response.PostListResponse;
import com.blog.domain.posts.api.dto.response.PostResponse;
import com.blog.domain.posts.domain.PostBlocks;
import com.blog.domain.posts.domain.Posts;
import com.blog.domain.posts.domain.repository.PostsRepository;
import com.blog.domain.users.service.UsersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostsService {

    private final UsersService usersService;
    private final PostsRepository postsRepository;
    private final PostBlockService postBlockService;
    private final CommentsService commentsService;

    public PostsService(UsersService usersService, PostBlockService postBlockService,
                        PostsRepository postsRepository, CommentsService commentsService){
        this.usersService = usersService;
        this.postBlockService = postBlockService;
        this.postsRepository = postsRepository;
        this.commentsService = commentsService;
    }

    // 게시글 등록
    public void createPost(int userId, PostsRequest request) {
        // 1. Posts 저장
        Posts post = Posts.createPost(userId, request.subject());
        int postId = postsRepository.addPost(post);

        // 2. PostBlocks 저장
        postBlockService.saveAllBlocks(postId, request.blocks());
    }

    // 게시글 목록 조회
    public PostListResponse getPostsList(){

        return new PostListResponse(postsRepository.getPostsList());
    }

    // 게시글 상세 조회
    public PostResponse getPostBlockListByPostId(int postId){
        Posts post = postsRepository.getPostByPostId(postId);

        String nickname = usersService.getUserNicknameByUserId(post.getUserId());

        List<PostBlocks> block = postBlockService.getPostBlockListByPostId(postId);

        // 댓글 조회
        List<CommentsResponse> comments = commentsService.getCommentsListByPostId(postId);

        return new PostResponse(
                postId,
                nickname,
                post.getSubject(),
                post.getCreatedAt(),
                block,
                comments
        );
    }

    // 게시글 수정 - 자신만 수정 가능
    @Transactional
    public void updatePost(int userId, int postId, PostsRequest request){

        validatePostOwnership(userId, postId);
        postsRepository.updatePost(postId, request.subject());
        postBlockService.updatePostBlock(postId, request.blocks());
    }

    // 글쓴이와 사용자 같은지
    public void validatePostOwnership(int userId, int postId) {
        int ownerId = postsRepository.getPostUserId(postId);

        if (userId != ownerId) {

            throw new CustomException(ErrorCode.NO_EDIT_PERMISSION);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(int userId, int postId){

        validatePostOwnership(userId, postId);
        postsRepository.deletePost(postId);
        postBlockService.deletePostBlock(postId);
    }
}

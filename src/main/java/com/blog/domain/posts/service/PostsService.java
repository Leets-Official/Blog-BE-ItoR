package com.blog.domain.posts.service;

import com.blog.common.response.ApiResponse;
import com.blog.domain.posts.api.dto.request.PostBlockRequest;
import com.blog.domain.posts.api.dto.request.PostCreateRequest;
import com.blog.domain.posts.api.dto.response.PostListResponse;
import com.blog.domain.posts.api.dto.response.PostResponse;
import com.blog.domain.posts.domain.PostBlocks;
import com.blog.domain.posts.domain.Posts;
import com.blog.domain.posts.domain.repository.PostsRepository;
import com.blog.domain.users.service.TokenService;
import com.blog.domain.users.service.UsersService;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsService {

    private final TokenService tokenService;
    private final UsersService usersService;
    private final PostsRepository postsRepository;
    private final PostBlockService postBlockService;

    public PostsService(TokenService tokenService, UsersService usersService,
                        PostBlockService postBlockService, PostsRepository postsRepository){
        this.tokenService = tokenService;
        this.usersService = usersService;
        this.postBlockService = postBlockService;
        this.postsRepository = postsRepository;
    }

    // userId 추출
    public int extractUserIdFromHeader(String authHeader){
        return tokenService.extractUserIdFromHeader(authHeader);
    }

    // 게시글 등록
    public void createPost(int userId, PostCreateRequest request) {
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

        return new PostResponse(
                postId,
                nickname,
                post.getSubject(),
                post.getCreatedAt(),
                block
        );
    }

}

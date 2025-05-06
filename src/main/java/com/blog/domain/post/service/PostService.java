package com.blog.domain.post.service;

import com.blog.domain.comment.controller.response.CommentResponse;
import com.blog.domain.comment.service.CommentService;
import com.blog.domain.post.controller.request.PostContentDto;
import com.blog.domain.post.controller.response.PostListResponse;
import com.blog.domain.post.controller.response.PostResponse;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.controller.request.PostRequest;
import com.blog.domain.post.domain.PostContent;
import com.blog.domain.post.repository.PostContentRepository;
import com.blog.domain.post.repository.PostRepository;
import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public PostService(PostRepository postRepository, PostContentRepository postContentRepository, UserRepository userRepository, CommentService commentService) {
        this.postRepository = postRepository;
        this.postContentRepository = postContentRepository;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

    public void createPost(Long userId, PostRequest postRequest) {
        PostValidator.validateUser(userRepository, userId);
        Post post = Post.of(userId, postRequest);
        Long postId = postRepository.save(post);

        // PostContent 저장
        postRequest.contents().forEach((contentDto) -> postContentRepository.save(PostContentMapper.mapFromDto(postId, contentDto)));
    }

    public PostResponse getPost(Long userId, Long postId) {
        User user = PostValidator.validateUser(userRepository, userId);  // User validation
        Post post = PostValidator.validatePostAccess(postRepository, postId, userId);  // Post & owner validation
        List<PostContent> postContent = postContentRepository.findByPostIdOrderBySequence(postId);

        boolean isOwner = post.getUserId().equals(userId);
        List<CommentResponse> commentResponse = commentService.getAllCommentsByPostId(postId);

        return PostResponse.withComments(PostResponse.from(post, postContent, user, isOwner), commentResponse);
    }

    public List<PostListResponse> getPostList(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);

        return posts.stream()
                .map(post -> {
                    List<PostContent> postContents = postContentRepository.findByPostIdOrderBySequence(post.getId());
                    return PostListResponse.from(post, postContents);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePost(Long userId, Long postId, @Valid PostRequest postRequest) {
        PostValidator.validatePostAccess(postRepository, postId, userId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        updatePostInfo(post, postRequest);
        updatePostContents(postId, postRequest);
    }

    private void updatePostInfo(Post post, PostRequest postRequest) {
        post.updateTitle(postRequest.title());
        post.updateUpdatedAt(LocalDateTime.now());
        postRepository.update(post);
    }

    @Transactional
    public void updatePostContents(Long postId, PostRequest postRequest) {
        List<PostContent> newContents = PostContentMapper.mapFromDtos(postId, postRequest.contents());
        postContentRepository.update(newContents);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        PostValidator.validatePostAccess(postRepository, postId, userId);  // User, Post, Owner validation
        commentService.deleteAllCommentsByPostId(postId);
        deletePostContents(postId);
        deletePostEntity(postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)));
    }

    @Transactional
    public void deletePostContents(Long postId) {
        postContentRepository.deleteByPostId(postId);
    }

    @Transactional
    public void deletePostEntity(Post post) {
        postRepository.deleteById(post.getId());
    }
}

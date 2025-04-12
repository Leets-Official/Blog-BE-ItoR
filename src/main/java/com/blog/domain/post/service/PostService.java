package com.blog.domain.post.service;

import com.blog.domain.post.controller.dto.request.PostContentDto;
import com.blog.domain.post.controller.dto.response.PostListResponse;
import com.blog.domain.post.controller.dto.response.PostResponse;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.controller.dto.request.PostRequest;
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
import java.util.stream.IntStream;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, PostContentRepository postContentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postContentRepository = postContentRepository;
        this.userRepository = userRepository;
    }


    // 글 저장
    @Transactional
    public void savePost(Long userId, @Valid PostRequest postRequest) {
        Post post = Post.of(userId, postRequest);
        Long postId = postRepository.save(post); // 먼저 저장하여 postId 확보

        int sequence = 0;
        for (PostContentDto postContentDto : postRequest.contents()) {
            PostContent content = createPostContent(postId, postContentDto, sequence++);
            postContentRepository.save(content);
        }
    }

    // 글 내용 저장 메서드 추출
    private PostContent createPostContent(Long postId, PostContentDto postContentDto, int sequence) {
        return switch (postContentDto.type()) {
            case TEXT -> PostContent.text(postId, postContentDto.data(), sequence);
            case IMAGE -> PostContent.image(postId, postContentDto.data(), sequence);
            default -> throw new CustomException(ErrorCode.POST_TYPE_NOT_FOUND);
        };
    }

    // 개별 조회
    public PostResponse getPost(Long userId, Long postId) {
        User user = validateUser(userId);
        Post post = validatePost(postId);
        List<PostContent> postContent = postContentRepository.findByPostIdOrderBySequence(postId);

        boolean isOwner = post.getUserId().equals(userId);// 본인 글인지 판단
        return PostResponse.from(post, postContent, user, isOwner);
    }


    // 전체 조회
    public List<PostListResponse> getPostList(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);

        // 유저가 쓴 글만 가져오기
        return posts.stream()
                .map(post -> {
                    List<PostContent> postContents = postContentRepository.findByPostIdOrderBySequence(post.getId());
                    return PostListResponse.from(post, postContents);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePost(Long userId, Long postId, @Valid PostRequest postRequest) {
        User user = validateUser(userId);
        Post post = validatePost(postId);
        validateOwner(post, userId);

        updatePostInfo(post, postRequest);
        updatePostContents(postId, postRequest);
    }

    private void updatePostInfo(Post post, PostRequest postRequest) {
        post.updateTitle(postRequest.title());
        post.updateUpdatedAt(LocalDateTime.now());
        postRepository.update(post);
    }

    private void updatePostContents(Long postId, PostRequest postRequest) {
        List<PostContent> newContents = IntStream.range(0, postRequest.contents().size())
                .mapToObj(i -> {
                    var dto = postRequest.contents().get(i);
                    return new PostContent(postId, dto.type(), dto.data(), i);
                })
                .collect(Collectors.toList());

        postContentRepository.update(newContents);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        User user = validateUser(userId);
        Post post = validatePost(postId);
        validateOwner(post, userId);

        deletePostContents(postId);
        deletePostEntity(post);
    }

    private void deletePostContents(Long postId) {
        postContentRepository.deleteByPostId(postId);
    }

    private void deletePostEntity(Post post) {
        postRepository.deleteById(post.getId());
    }

    // 검증 메서드
    private User validateUser(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Post validatePost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateOwner(Post post, Long userId) {
        if (!post.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_POST_ACCESS);
        }
    }


}

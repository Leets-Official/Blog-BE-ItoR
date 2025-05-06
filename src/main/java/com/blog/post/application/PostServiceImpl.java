package com.blog.post.application;

import com.blog.post.domain.*;
import com.blog.post.presentation.dto.PostContentDto;
import com.blog.post.presentation.dto.PostRequest;
import com.blog.post.presentation.dto.PostResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostResponse createPost(PostRequest request, int userId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Post post = buildPostFromRequest(request, userId, now);
        postRepository.save(post);

        List<PostContent> contents = convertToPostContents(request, post.getPostId(), userId);
        post.setContents(contents);
        postRepository.saveContents(contents);

        return convertToPostResponse(post);
    }

    @Override
    public PostResponse getPostById(int postId) {
        Post post = postRepository.findById(postId);
        return convertToPostResponse(post);
    }

    @Override
    public PostResponse updatePost(int postId, PostRequest request, int userId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Post post = new Post(postId, userId, request.getTitle(), null, now, null);
        postRepository.deleteContents(postId, userId);

        List<PostContent> contents = convertToPostContents(request, postId, userId);
        postRepository.saveContents(contents);

        Post updated = postRepository.findById(postId);
        return convertToPostResponse(updated);
    }

    @Override
    public void deletePost(int postId, int userId) {
        postRepository.deleteContents(postId, userId);
        postRepository.delete(postId, userId);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
    }

    private Post buildPostFromRequest(PostRequest request, int userId, Timestamp now) {
        return new Post(0, userId, request.getTitle(), now, now, new ArrayList<>());
    }

    private List<PostContent> convertToPostContents(PostRequest request, int postId, int userId) {
        return request.getContents().stream()
                .map(dto -> new PostContent(
                        0,
                        postId,
                        userId,
                        PostContentType.from(dto.getContentType()),
                        dto.getContent(),
                        dto.getOrder()
                )).collect(Collectors.toList());
    }

    private PostResponse convertToPostResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setPostId(post.getPostId());
        response.setUserId(post.getUserId());
        response.setTitle(post.getTitle());
        response.setCreatedAt(post.getCreatedAt().toString());

        List<PostContentDto> contentDtos = post.getContents().stream()
                .map(c -> {
                    PostContentDto dto = new PostContentDto();
                    dto.setContentType(c.getContentType().getValue());
                    dto.setContent(c.getContent());
                    dto.setOrder(c.getOrder());
                    return dto;
                }).collect(Collectors.toList());

        response.setContents(contentDtos);
        return response;
    }
}

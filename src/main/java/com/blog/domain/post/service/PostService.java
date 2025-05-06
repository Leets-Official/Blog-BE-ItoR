package com.blog.domain.post.service;

import com.blog.domain.post.domain.Post;
import com.blog.domain.post.dto.PostRequest;
import com.blog.domain.post.dto.PostResponse;
import com.blog.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {
  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public void createPost(PostRequest request) {
    Post post = new Post(
        UUID.randomUUID(),
        request.getTitle(),
        request.getContent(),
        request.getAuthorId(),
        LocalDateTime.now(),
        LocalDateTime.now()
    );
    postRepository.save(post);
  }

  public List<PostResponse> getAllPosts() {
    return postRepository.findAll()
      .stream()
      .map(PostResponse::from)
      .collect(Collectors.toList());
  }

  public PostResponse getPostById(UUID id) {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    return PostResponse.from(post);
  }

  public void updatePost(UUID id, PostRequest request) {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

    Post updated = new Post(
        post.getId(),
        request.getTitle(),
        request.getContent(),
        post.getAuthorId(),
        post.getCreatedAt(),
        LocalDateTime.now()
    );


    postRepository.update(updated);
  }

  public void deletePost(UUID id) {
    postRepository.deleteById(id);
  }

  public List<PostResponse> getPostsByAuthor(UUID authorId) {
    return postRepository.findByAuthorId(authorId.toString())
        .stream()
        .map(PostResponse::from)
        .collect(Collectors.toList());
  }

}

package com.blog.domain.post.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.domain.post.domain.ContentBlock;
import com.blog.domain.post.domain.Post;
import com.blog.domain.post.domain.PostImage;
import com.blog.domain.post.dto.PostDetailResponseDto;
import com.blog.domain.post.dto.PostRequestDto;
import com.blog.domain.post.dto.PostResponseDto;
import com.blog.domain.post.repository.PostImageRepository;
import com.blog.domain.post.repository.PostRepository;
import com.blog.domain.user.service.UserService;
import com.blog.global.common.dto.PageRequestDto;
import com.blog.global.common.dto.PageResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.blog.global.config.error.ErrorCode;
import com.blog.global.config.error.exception.CommonException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final ObjectMapper objectMapper;
	private final UserService userService;
	//댓글 추후 추가 예정

	public PostService(PostRepository postRepository, PostImageRepository postImageRepository,
		ObjectMapper objectMapper, UserService userService) {
		this.postRepository = postRepository;
		this.postImageRepository = postImageRepository;
		this.objectMapper = objectMapper;
		this.userService = userService;
	}

	@Transactional //글 생성
	public void createPost(int userId, PostRequestDto postRequestDto) {
		String contentJson = serialize(postRequestDto.getContentBlocks());

		Post post = new Post(0, userId, postRequestDto.getTitle(), contentJson, now(), now(), null);
		boolean inserted = postRepository.insert(post);
		if (!inserted) {
			throw new CommonException(ErrorCode.POST_CREATE_FAILED);
		}

		for (ContentBlock block : postRequestDto.getContentBlocks()) {
			if ("image".equals(block.getType())) {
				PostImage image = new PostImage(0, post.getPostId(), block.getValue(), now());
				postImageRepository.insert(image);
			}
		}

	}

	// 게시물 조회
	public PostDetailResponseDto getPostById(int postId) {
		Post post = postRepository.findById(postId);
		if (post == null || post.getDeletedAt() != null) {
			throw new CommonException(ErrorCode.POST_NOT_FOUND);
		}

		List<ContentBlock> content = deserialize(post.getContent());
		String nickname = userService.findNicknameByUserId(post.getUserId());
		int commentCount = 0;

		return new PostDetailResponseDto(
			post.getPostId(),
			nickname,
			post.getCreatedAt(),
			commentCount,
			post.getTitle(),
			content,
			List.of()
		);
	}

	@Transactional //게시물 수정
	public void updatePost(int userId,int postId, PostRequestDto postRequestDto) {
		Post post = postRepository.findById(postId);
		if (post == null || post.getDeletedAt() != null) {
			throw new CommonException(ErrorCode.POST_NOT_FOUND);
		}
		if (post.getUserId() != userId) {
			throw new CommonException(ErrorCode.FORBIDDEN_POST_ACCESS);
		}
		Post updated = new Post(postId, userId, postRequestDto.getTitle(), serialize(postRequestDto.getContentBlocks()), post.getCreatedAt(), now(), null);

		boolean updatedResult = postRepository.update(updated);
		if (!updatedResult) {
			throw new CommonException(ErrorCode.POST_NOT_FOUND);
		}
	}

	@Transactional
	public void deletePost(int userId, int postId) {
		Post post = postRepository.findById(postId);
		if (post == null || post.getDeletedAt() != null)
			throw new CommonException(ErrorCode.POST_NOT_FOUND);

		if (post.getUserId() != userId)
			throw new CommonException(ErrorCode.FORBIDDEN_POST_ACCESS);

		boolean deleted = postRepository.softDelete(postId);
		if (!deleted)
			throw new CommonException(ErrorCode.POST_NOT_FOUND);
	}

	//  직렬화 (ContentBlock → JSON)
	private String serialize(List<ContentBlock> blocks) {
		try {
			return objectMapper.writeValueAsString(blocks);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.SERIALIZE_FAILED);
		}
	}

	//  역직렬화 (JSON → ContentBlock)
	private List<ContentBlock> deserialize(String json) {
		try {
			return objectMapper.readValue(json, new TypeReference<>() {});
		} catch (Exception e) {
			return List.of(); // 실패 시 빈 목록 반환
		}
	}

	private LocalDateTime now() {
		return LocalDateTime.now();
	}

	public PageResponseDto<PostResponseDto> getPostPage(PageRequestDto pageRequestDto) {
		int page = pageRequestDto.getPage();
		int size = pageRequestDto.getSize();
		int offset = (page - 1) * size;

		List<Post> posts = postRepository.findPage(offset, size);
		int total = postRepository.countAll();

		List<PostResponseDto> data = posts.stream()
			.map(post -> new PostResponseDto(
				post.getPostId(),
				userService.findNicknameByUserId(post.getUserId()),
				post.getCreatedAt(),
				0,
				post.getTitle(),
				deserialize(post.getContent())
			))
			.toList();

		return new PageResponseDto<>(page, size, total, data);
	}

}

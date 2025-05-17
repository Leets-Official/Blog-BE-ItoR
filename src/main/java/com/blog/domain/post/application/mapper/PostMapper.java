package com.blog.domain.post.application.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.blog.domain.post.application.dto.ContentDTO;
import com.blog.domain.post.application.dto.PostDTO;
import com.blog.domain.post.domain.entity.Post;
import com.blog.domain.user.domain.entity.User;

@Component
public class PostMapper {

	public Post fromDTO(PostDTO.Save dto, User user) {
		return new Post(user, dto.title());
	}

	public PostDTO.Response toResponse(Post post, List<ContentDTO.Response> contentResponse) {
		return new PostDTO.Response(post.getId(), post.getTitle(), post.getUser().getNickname(),
			post.getCommentCount(), contentResponse, post.getCreatedAt(), post.getUpdatedAt());
	}

	public PostDTO.ResponseAll toResponseAll(Post post, List<ContentDTO.Response> contentResponse) {
		return new PostDTO.ResponseAll(post.getId(), post.getTitle(), post.getUser().getNickname(),
			post.getCommentCount(), contentResponse, post.getCreatedAt(), post.getUpdatedAt());
	}
}

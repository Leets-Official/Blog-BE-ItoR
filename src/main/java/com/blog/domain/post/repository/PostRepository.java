package com.blog.domain.post.repository;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import com.blog.domain.post.domain.Post;
import com.blog.domain.post.dto.PostSummaryDto;

public interface PostRepository {

	boolean insert(Post post);
	Optional<Post> findById(int postId);
	List<Post> findPage(int offset, int size);
	int countAll ();
	boolean update(Post post);
	boolean softDelete(int postId);
	List<PostSummaryDto> findAllByUserId(int userId);

}

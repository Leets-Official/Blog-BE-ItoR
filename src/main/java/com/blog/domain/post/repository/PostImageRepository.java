package com.blog.domain.post.repository;

import java.util.List;

import com.blog.domain.post.domain.PostImage;

public interface PostImageRepository {

	boolean insert(PostImage image);
	List<PostImage> findByPostId(int postId);

}

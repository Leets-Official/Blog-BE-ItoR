package com.blog.domain.post.repository;

import java.sql.Connection;
import java.util.List;

import com.blog.domain.post.domain.Post;

public interface PostRepository {

	boolean insert(Post post);
	Post findById(int postId);
	List<Post> findPage(int offset, int size);
	int countAll ();
	boolean update(Post post);
	boolean softDelete(int postId);

}

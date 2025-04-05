package com.blog.domain.post.repository;

import java.sql.Connection;
import java.util.List;

import com.blog.domain.post.domain.Post;

public interface PostRepository {

	boolean insert(Connection conn, Post post);
	Post findById(Connection conn, int postId);
	List<Post> findPage(Connection conn, int offset, int size);
	int countAll (Connection conn);
	boolean update(Connection conn, Post post);
	boolean Softdelete(Connection conn, int postId);

}

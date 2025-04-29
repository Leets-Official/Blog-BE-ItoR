package com.blog.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import com.blog.domain.comment.domain.Comment;
import com.blog.domain.comment.dto.CommentResponseDto;

public interface CommentRepository {

	void save(Comment comment);
	List<Comment> findByPostId(int postId);
	boolean update(Comment comment);
	boolean delete(int commentId);
	Optional<Comment> findById(int commentId);


}

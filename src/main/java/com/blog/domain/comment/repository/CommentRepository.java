package com.blog.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import com.blog.domain.comment.domain.Comment;

public interface CommentRepository {
	//어떤 메서드들이 필요할까 CRUD - 고민하기
	void save(Comment comment);
	List<Comment> findByPostId(int postId);
	boolean update(Comment comment);
	boolean delete(int commentId);


}

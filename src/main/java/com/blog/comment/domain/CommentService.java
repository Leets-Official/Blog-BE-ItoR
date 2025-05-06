package com.blog.comment.domain;

import com.blog.comment.presentation.dto.CommentCreateRequest;
import com.blog.comment.presentation.dto.CommentUpdateRequest;
import com.blog.comment.presentation.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(int postId, CommentCreateRequest request, int userId);

    List<CommentResponse> getComments(int postId);

    CommentResponse updateComment(int postId, int commentId, CommentUpdateRequest request, int userId);

    void deleteComment(int postId, int commentId, int userId);
}

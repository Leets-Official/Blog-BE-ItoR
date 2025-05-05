package com.blog.domain.comments.service;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.domain.comments.api.dto.request.CommentsRequest;
import com.blog.domain.comments.api.dto.response.CommentsResponse;
import com.blog.domain.comments.domain.Comments;
import com.blog.domain.comments.domain.repository.CommentsRepository;
import com.blog.domain.users.domain.Users;
import com.blog.domain.users.service.UsersService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {

    private final UsersService usersService;
    private final CommentsRepository commentsRepository;

    public CommentsService(UsersService usersService, CommentsRepository commentsRepository){
        this.usersService = usersService;
        this.commentsRepository = commentsRepository;
    }

    public void addComments(int userId, CommentsRequest request){
        commentsRepository.addComments(userId, request);
    }

    public List<CommentsResponse> getCommentsListByPostId(int postId){
        return commentsRepository.getCommentsListByPostId(postId);
    }

    public void updateComment(int userId, int commentId, CommentsRequest request){

        validateCommentOwnership(userId, commentId);

        commentsRepository.updateComment(commentId, request);
    }

    public void deleteComment(int userId, int commentId){

        validateCommentOwnership(userId, commentId);

        commentsRepository.deleteComment(commentId);
    }

    // 글쓴이와 사용자 같은지
    public void validateCommentOwnership(int userId, int commentId) {
        int ownerId = commentsRepository.getCommentsUserId(commentId);

        if (userId != ownerId) {

            throw new CustomException(ErrorCode.NO_EDIT_PERMISSION);
        }
    }
}

package com.blog.domain.comments.service;

import com.blog.domain.comments.api.dto.request.CommentsRequest;
import com.blog.domain.comments.domain.repository.CommentsRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository){
        this.commentsRepository = commentsRepository;
    }

    public void addComments(int userId, CommentsRequest request){
        commentsRepository.addComments(userId, request);
    }

}

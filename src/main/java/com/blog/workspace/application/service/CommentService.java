package com.blog.workspace.application.service;

import com.blog.workspace.application.in.comment.CommentUseCase;
import com.blog.workspace.application.out.comment.DeleteCommentPort;
import com.blog.workspace.application.out.comment.LoadCommentPort;
import com.blog.workspace.application.out.comment.SaveCommentPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService implements CommentUseCase {

    private final SaveCommentPort savePort;
    private final LoadCommentPort loadPort;
    private final DeleteCommentPort deletePort;

    public CommentService(SaveCommentPort savePort, LoadCommentPort loadPort, DeleteCommentPort deletePort) {
        this.savePort = savePort;
        this.loadPort = loadPort;
        this.deletePort = deletePort;
    }

    /// 비즈니스 로직

}

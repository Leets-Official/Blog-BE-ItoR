package com.blog.workspace.adapter.out;

import com.blog.workspace.adapter.out.jdbc.comment.CommentJdbcRepository;
import com.blog.workspace.application.out.comment.DeleteCommentPort;
import com.blog.workspace.application.out.comment.LoadCommentPort;
import com.blog.workspace.application.out.comment.SaveCommentPort;
import com.blog.workspace.domain.comment.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentPersistenceAdapter implements SaveCommentPort, LoadCommentPort, DeleteCommentPort {

    /*
        JDBC를 통해 구축하는 Adapter 입니다.
     */

    private final CommentJdbcRepository repository;

    public CommentPersistenceAdapter(CommentJdbcRepository repository) {
        this.repository = repository;
    }

    /// 로직
    @Override
    public Comment saveComment(Comment comment) {
        return null;
    }

    @Override
    public Comment updateComment(Comment comment) {
        return null;
    }

    @Override
    public void deleteById(Long commentId) {

    }

    @Override
    public List<Comment> loadCommentsByBoardId(Long boardId) {
        return List.of();
    }

    @Override
    public List<Comment> loadCommentsByCommentId(String parentId) {
        return List.of();
    }

    @Override
    public boolean checkCommentByUserId(Long userId, Long commentId) {
        return false;
    }

}

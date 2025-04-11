package com.blog.workspace.adapter.out;

import com.blog.workspace.adapter.out.jdbc.comment.CommentJdbc;
import com.blog.workspace.adapter.out.jdbc.comment.CommentJdbcRepository;
import com.blog.workspace.application.out.comment.DeleteCommentPort;
import com.blog.workspace.application.out.comment.LoadCommentPort;
import com.blog.workspace.application.out.comment.SaveCommentPort;
import com.blog.workspace.domain.comment.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentPersistenceAdapter implements SaveCommentPort, LoadCommentPort, DeleteCommentPort {

    /*
        JDBC를 통해 구축하는 Adapter 입니다.
     */

    private final CommentJdbcRepository repository;

    public CommentPersistenceAdapter(CommentJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comment saveComment(Comment comment) {
        var entity = CommentJdbc.from(comment);

        return repository.save(entity)
                .toDomain();
    }

    @Override
    public Comment updateComment(Comment comment) {
        var entity = CommentJdbc.fromDB(comment.getId(), comment.getPostId(), comment.getUserId(), comment.getContent(), comment.getCreated(), comment.getUpdated());

        return repository.update(entity)
                .toDomain();
    }

    @Override
    public void deleteById(Long commentId) {
        repository.deleteById(commentId);
    }

    @Override
    public Optional<Comment> loadComment(Long commentId) {
        return repository.findById(commentId)
                .map(CommentJdbc::toDomain);
    }

    @Override
    public List<Comment> loadCommentsByPostId(Long postId) {

        // 불변을 위해 .toList로
        return repository.findByPostId(postId)
                .stream().map(CommentJdbc::toDomain)
                .toList();
    }

}

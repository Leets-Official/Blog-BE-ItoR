package com.blog.workspace.adapter.out;

import com.blog.workspace.adapter.out.jdbc.post.PostJdbcRepository;
import com.blog.workspace.application.out.post.DeletePostPort;
import com.blog.workspace.application.out.post.LoadPostPort;
import com.blog.workspace.application.out.post.SavePostPort;
import com.blog.workspace.domain.post.Post;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostPersistenceAdapter implements SavePostPort, LoadPostPort, DeletePostPort {

    /*
        JDBC를 통해 구축하는 Adapter 입니다.
     */

    private final PostJdbcRepository repository;

    public PostPersistenceAdapter(PostJdbcRepository repository) {
        this.repository = repository;
    }

    /// 로직

    @Override
    public Post savePost(Post post) {
        return null;
    }

    @Override
    public Post updatePost(Post post) {
        return null;
    }

    @Override
    public Optional<Post> loadPost(Long postId) {
        return Optional.empty();
    }

    @Override
    public List<Post> loadPosts() {
        return List.of();
    }

    @Override
    public boolean checkPostByUserId(Long userId, Long postId) {
        return false;
    }


}

package com.blog.workspace.adapter.out;

import com.blog.common.response.page.Page;
import com.blog.common.response.page.Pageable;
import com.blog.workspace.adapter.out.jdbc.post.PostJdbc;
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
        var entity = PostJdbc.from(post);

        return repository.save(entity)
                .toDomain();
    }

    @Override
    public Post updatePost(Post post) {
        var entity = PostJdbc.forDB(post);

        return repository.update(entity)
                .toDomain();
    }

    @Override
    public Optional<Post> loadPost(Long postId) {

        return repository.findById(postId)
                .map(PostJdbc::toDomain);
    }

    @Override
    public Page<Post> loadPosts(Pageable pageable) {
        return null;
    }

    @Override
    public boolean checkPostByUserId(Long userId, Long postId) {

        return repository.existsByUserIdAndPostId(userId, postId);
    }


    @Override
    public void deletePostById(Long id) {
        repository.deleteById(id);
    }
}

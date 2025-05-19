package com.blog.workspace.adapter.out;

import com.blog.workspace.adapter.out.jdbc.post.ContentBlockJdbc;
import com.blog.workspace.adapter.out.jdbc.post.ContentBlockJdbcRepository;
import com.blog.workspace.application.out.image.ImagePort;
import com.blog.workspace.application.out.post.ContentBlockPort;
import com.blog.workspace.domain.post.ContentBlock;
import com.blog.workspace.domain.post.ContentType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContentBlockPersistenceAdapter implements ContentBlockPort {

    private final ContentBlockJdbcRepository repository;
    private final ImagePort imagePort;

    public ContentBlockPersistenceAdapter(ContentBlockJdbcRepository repository, ImagePort imagePort) {
        this.repository = repository;
        this.imagePort = imagePort;
    }

    @Override
    public ContentBlock saveBlock(ContentBlock contentBlock) {

        var entity = ContentBlockJdbc.from(contentBlock);

        return repository.save(entity)
                .toDomain();
    }

    @Override
    public List<ContentBlock> loadBlocks(Long postId) {
        return repository.findByPostId(postId)
                .stream().map(ContentBlockJdbc::toDomain)
                .toList();
    }

    @Override
    public void deleteBlockById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteBlockByPost(Long postId) {

        /// S3에서 이미지 삭제
        List<ContentBlock> blockList = loadBlocks(postId);
        for (ContentBlock block : blockList) {
            if (block.getType() == ContentType.IMAGE){
                imagePort.deleteFile(block.getContent());
            }
        }

        repository.deleteByPostId(postId);
    }

    @Override
    public void changeBlockOrder(ContentBlock contentBlock) {

    }
}

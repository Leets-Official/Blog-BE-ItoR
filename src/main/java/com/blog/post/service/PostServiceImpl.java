package com.blog.post.service;

import com.blog.post.dao.PostDao;
import com.blog.post.record.PostRecord;
import com.blog.post.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {


    @Autowired
    private PostDao dao;

    @Override
    public List<PostRecord> getPostList(int page, int size) {
        return dao.getPostList(page, size);
    }

    @Override
    public PostDetailVo getPostDetail(int postNum) {
        return dao.getPostDetail(postNum);
    }

    @Override
    public Long createPost(PostWriteVo vo) {
        return dao.insertPost(vo);
    }

    @Override
    public void editPost(PostEditVo vo) {
        String writerId = dao.getAuthorId(vo.getPostNum());
        if (!writerId.equals(vo.getAuthorId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        dao.updatePost(vo);
    }

    @Override
    public void deletePost(Long postId, String authorId) {
        String writerId = dao.getAuthorId(postId);
        if (!writerId.equals(authorId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        dao.deletePost(postId, authorId);
    }



}//class

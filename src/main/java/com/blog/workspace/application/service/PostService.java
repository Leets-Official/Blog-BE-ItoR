package com.blog.workspace.application.service;

import com.blog.workspace.application.in.post.PostUseCase;
import com.blog.workspace.application.out.post.DeletePostPort;
import com.blog.workspace.application.out.post.LoadPostPort;
import com.blog.workspace.application.out.post.SavePostPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService implements PostUseCase {

    private final SavePostPort savePort;
    private final LoadPostPort loadPort;
    private final DeletePostPort deletePort;

    /// 생성자
    public PostService(SavePostPort savePort, LoadPostPort loadPort, DeletePostPort deletePort) {
        this.savePort = savePort;
        this.loadPort = loadPort;
        this.deletePort = deletePort;
    }

    /// 비즈니스 로직


}

package com.blog.workspace.application.service;

import com.blog.workspace.application.in.UserUseCase;
import com.blog.workspace.application.out.user.UserPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserUseCase {

    private final UserPort savePort;


    public UserService(UserPort savePort) {
        this.savePort = savePort;
    }


    /// 비즈니스 로직
}

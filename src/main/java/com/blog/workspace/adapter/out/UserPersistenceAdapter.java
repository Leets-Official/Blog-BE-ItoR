package com.blog.workspace.adapter.out;

import com.blog.workspace.adapter.out.jdbc.user.UserJdbcRepository;
import com.blog.workspace.application.out.user.UserPort;
import com.blog.workspace.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserPort {

    /*
        JDBC를 통해 구축하는 Adapter 입니다.
     */

    private final UserJdbcRepository repository;

    public UserPersistenceAdapter(UserJdbcRepository repository) {
        this.repository = repository;
    }

    // 저장하기
    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public Optional<User> findMe(Long userId) {
        return Optional.empty();
    }
}

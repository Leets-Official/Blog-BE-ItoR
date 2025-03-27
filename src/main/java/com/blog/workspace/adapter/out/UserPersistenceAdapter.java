package com.blog.workspace.adapter.out;

import com.blog.workspace.adapter.out.jdbc.user.UserJdbc;
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

        var entity = UserJdbc.from(user);

        return repository.save(entity)
                .toDomain();
    }

    @Override
    public void updateUser(User user) {
        var entity = UserJdbc.forUpdate(user);
        repository.updateUser(entity);

    }

    @Override
    public boolean loadExistingEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<User> findMe(Long userId) {
        return repository.findById(userId)
                .map(UserJdbc::toDomain);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserJdbc::toDomain);
    }
}

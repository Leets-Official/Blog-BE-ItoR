package com.blog.workspace.adapter.out;

import com.blog.workspace.adapter.out.jdbc.token.JwtTokenJdbc;
import com.blog.workspace.adapter.out.jdbc.token.JwtTokenJdbcRepository;
import com.blog.workspace.application.out.token.TokenPort;
import com.blog.workspace.domain.token.JwtToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenPersistenceAdapter implements TokenPort {

    private final JwtTokenJdbcRepository repository;

    /// 생성자
    public TokenPersistenceAdapter(JwtTokenJdbcRepository repository) {
        this.repository = repository;
    }

    // 저장하기
    @Override
    public JwtToken saveToken(JwtToken jwtToken) {
        var jwtTokenJdbc = JwtTokenJdbc.from(jwtToken);
        return repository.upsert(jwtTokenJdbc).toDomain();
    }

    // 리프레쉬 토큰 바탕으로 userId
    @Override
    public Optional<Long> getUserByToken(String refreshToken) {
        return repository.findUserIdByRefreshToken(refreshToken);
    }


    @Override
    public void deleteByToken(String refreshToken) {
        repository.deleteByRefreshToken(refreshToken);
    }

}

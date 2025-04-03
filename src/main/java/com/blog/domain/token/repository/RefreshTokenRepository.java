package com.blog.domain.token.repository;

import java.util.Optional;
import com.blog.domain.token.domain.RefreshToken;

public interface RefreshTokenRepository {
	Optional<RefreshToken> findByUserId(int userId);
	void save(RefreshToken token);
	void deleteByUserId(int userId);
	void update(RefreshToken refreshToken);
	boolean existsByUserId(int userId);
}

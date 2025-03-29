package com.blog.domain.user.repository;

import java.util.Optional;

import com.blog.domain.user.domain.User;

public interface UserRepository {

	void save(User user);
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}

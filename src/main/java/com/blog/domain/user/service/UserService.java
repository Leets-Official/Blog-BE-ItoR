package com.blog.domain.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.domain.UserType;
import com.blog.domain.user.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

}

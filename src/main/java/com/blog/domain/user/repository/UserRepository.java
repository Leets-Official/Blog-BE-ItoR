package com.blog.domain.user.repository;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    // 이메일 회원가입 처리
    public void save(JoinRequest joinRequest) {
        String sql = "INSERT INTO user (email, password, name, nickname, introduction, birth, profile_image, provider) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Date sqlBirth = joinRequest.getBirth() != null ? Date.valueOf(joinRequest.getBirth()) : null;

        jdbcTemplate.update(
                sql,
                joinRequest.getEmail(),
                joinRequest.getPassword(),
                joinRequest.getName(),
                joinRequest.getNickname(),
                joinRequest.getIntroduction(),
                sqlBirth,
                joinRequest.getProfileImage(),
                joinRequest.getProvider()
        );
    }

}

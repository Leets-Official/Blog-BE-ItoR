package com.blog.domain.user.repository;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import org.springframework.dao.DataAccessException;
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

    // 카카오에서 받은 이메일로 유저 존재하는지 가져오기
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM `user` WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{email},
                    new BeanPropertyRowMapper<>(User.class)
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {  // DB 접근 에러 처리
            throw new RuntimeException("데이터베이스 조회 중 오류 발생", e);
        }
    }

}

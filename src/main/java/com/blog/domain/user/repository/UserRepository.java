package com.blog.domain.user.repository;

import com.blog.domain.user.controller.dto.request.JoinRequest;
import com.blog.domain.user.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    public void save(User joinUser) {
        String sql = "INSERT INTO user (email, password, name, nickname, introduction, birth, profile_image, provider) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        java.sql.Date sqlBirth = null;
        if (joinUser.getBirth() != null) {
            sqlBirth = java.sql.Date.valueOf(joinUser.getBirth());
        }

        jdbcTemplate.update(
                sql,
                joinUser.getEmail(),
                joinUser.getPassword(),
                joinUser.getName(),
                joinUser.getNickname(),
                joinUser.getIntroduction(),
                sqlBirth,
                joinUser.getProfileImage(),
                String.valueOf(joinUser.getProvider()) // provider가 객체일 경우 대비하여 문자열 변환
        );
    }


    //  이메일로 유저 존재하는지 가져오기
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM `user` WHERE email = ?";

        System.out.println("이메일 조회 요청: " + email); // 로그 추가

        try {
            RowMapper<User> rowMapper = (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            };

            User user = jdbcTemplate.queryForObject(sql, new Object[]{email}, rowMapper);
            System.out.println("조회된 사용자: " + user);
            return Optional.ofNullable(user);

        } catch (EmptyResultDataAccessException e) {
            System.out.println("사용자를 찾을 수 없음: " + email);
            return Optional.empty();
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 조회 중 오류 발생", e);
        }
    }



}

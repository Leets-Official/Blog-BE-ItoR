package com.blog.domain.user.repository;

import com.blog.domain.user.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
                String.valueOf(joinUser.getProvider())
        );
    }


    //  이메일로 유저 존재하는지 가져오기
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM `user` WHERE email = ?";

        try {
            RowMapper<User> rowMapper = (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"));

            User user = jdbcTemplate.queryForObject(sql, new Object[]{email}, rowMapper);
            return Optional.ofNullable(user);

        } catch (DataAccessException e) {
            throw new RuntimeException("데이터베이스 조회 중 오류 발생", e);
        }
    }


}

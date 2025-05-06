package com.blog.domain.user.repository;

import com.blog.domain.user.domain.User;
import com.blog.global.exception.CustomException;
import com.blog.global.exception.ErrorCode;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

        LocalDateTime birth = joinUser.getBirth(); // LocalDateTime
        java.sql.Date sqlBirth = (birth != null) ? java.sql.Date.valueOf(birth.toLocalDate()) : null;


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

    //  유저Id로 유저 가져오기
    public Optional<User> findByUserId(Long userId) {
        String sql = "SELECT * FROM `user` WHERE id = ?";
        try {
            RowMapper<User> rowMapper = (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("nickname"),
                    rs.getDate("birth") != null ? rs.getDate("birth").toLocalDate().atStartOfDay() : null,
                    rs.getString("introduction"),
                    rs.getString("profile_image"),
                    rs.getString("provider")
            );

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{userId}, rowMapper));

        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void deleteUser(Long userId) {
        String sql = "DELETE FROM user WHERE id = ?";
        System.out.println("delete userId : " + userId);

        try {
            int deletedCount = jdbcTemplate.update(sql, userId);

            if (deletedCount == 0) {
                // 삭제된 행이 없다면 예외 발생
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }
        } catch (DataAccessException e) {
            // 데이터 접근 오류 시 예외 처리
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN); // 예외 코드에 맞게 변경
        }
    }

    // 마이페이지 정보 가져오기
    public Optional<User> findByUserIdForMyPage(Long userId) {
        String sql = "SELECT * FROM `user` WHERE id = ?";
        try {
            RowMapper<User> rowMapper = (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("nickname"),
                    rs.getDate("birth") != null ? rs.getDate("birth").toLocalDate().atStartOfDay() : null,
                    rs.getString("introduction"),
                    rs.getString("profile_image"),
                    rs.getString("provider")
            );

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{userId}, rowMapper));

        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    // 마이페이지 수정
    public User updateUser(User user) {
        String sql = "UPDATE user SET name = ?, nickname = ?, introduction = ?, birth = ?, profile_image = ? WHERE id = ?";
        LocalDateTime birth = user.getBirth(); // LocalDateTime
        java.sql.Date sqlBirth = (birth != null) ? java.sql.Date.valueOf(birth.toLocalDate()) : null;

        jdbcTemplate.update(
                sql,
                user.getName(),
                user.getNickname(),
                user.getIntroduction(),
                sqlBirth,
                user.getProfileImage(),
                user.getId()
        );
        return user;
    }

}



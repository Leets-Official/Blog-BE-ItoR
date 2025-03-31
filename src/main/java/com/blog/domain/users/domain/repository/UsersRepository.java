package com.blog.domain.users.domain.repository;

import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.login.api.dto.request.LoginRequest;
import com.blog.domain.users.api.dto.request.UsersIdRequest;
import com.blog.domain.users.api.dto.request.UsersUpdateRequest;
import com.blog.domain.users.api.dto.response.UsersInfoResponse;
import com.blog.domain.users.api.dto.response.UsersResultResponse;
import com.blog.domain.users.domain.Users;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public class UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsersRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 사용자 등록
    public int emailRegister(AuthEmailRequest request) {

        String sql = "INSERT INTO users (email, password, name, nickname, birth, profile_image, social, introduce) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                request.email(),
                request.password(),
                request.name(),
                request.nickname(),
                request.birth(),
                request.profile_image(),
                false,  // social: 기본값은 false
                request.introduce()
        );

        // 삽입된 레코드의 마지막 ID를 가져오는 쿼리
        String selectSql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(selectSql, Integer.class);
    }

    // 이메일 중복확인
    public boolean isEmailDuplicated(AuthEmailRequest request) {

        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, request.email());

        return count != null && count > 0;
    }

    // 닉네임 중복확인
    public boolean isNickNameDuplicated(String nickname) {

        String sql = "SELECT COUNT(*) FROM users WHERE nickname = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nickname);

        return count != null && count > 0;
    }

    // 사용자 정보 수정
    public int usersUpdateInfo(UsersUpdateRequest request){
        String sql = "UPDATE users SET password = ?, nickname = ?, profile_image = ? WHERE id = ?";

        return jdbcTemplate.update(sql,
                request.password(),
                request.nickname(),
                request.profile_image(),
                request.user_id()
        );
    }

    // 사용자 정보 조회
    public UsersInfoResponse usersInfo(UsersIdRequest request){
        String sql = "SELECT * FROM users WHERE id = ?";

        try {

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UsersInfoResponse(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("nickname"),
                    rs.getObject("birth", LocalDate.class),
                    rs.getString("profile_image"),
                    rs.getBoolean("social"),
                    rs.getString("introduce")
            ), request.user_id());
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }

    // 사용자 삭제
    public UsersResultResponse usersDeleteInfo(UsersIdRequest request){
        String sql = "DELETE FROM users WHERE id = ?";

        int result = jdbcTemplate.update(sql, request.user_id());
        // 삭제 X
        if (result == 0) {
            return new UsersResultResponse(0);
        }

        return new UsersResultResponse(result);
    }


    // 로그인
    public Users emailLogin(LoginRequest request){
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Users(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("nickname"),
                    rs.getString("password"),
                    rs.getString("profile_image"),
                    rs.getBoolean("social"),
                    rs.getString("introduce"),
                    rs.getObject("birth", LocalDate.class),
                    rs.getObject("created_at", LocalDateTime.class),
                    rs.getObject("updated_at", LocalDateTime.class)
            ), request.email(), request.password());
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }
}

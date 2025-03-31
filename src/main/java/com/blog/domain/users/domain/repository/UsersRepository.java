package com.blog.domain.users.domain.repository;

import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.users.api.dto.request.UsersInfoRequest;
import com.blog.domain.users.api.dto.request.UsersUpdateRequest;
import com.blog.domain.users.api.dto.response.UsersInfoResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsersRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 사용자 등록 (INSERT)
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
        String sql = "UPDATE FROM users SET password = ?, nickname = ?, profile_image = ? WHERE user_id = ?";

        return jdbcTemplate.update(sql,
                request.password(),
                request.nickname(),
                request.profile_image(),
                request.user_id()  // user_id로 WHERE 절을 통해 특정 사용자 업데이트
        );
    }

    // 사용자 정보 조회
    public UsersInfoResponse usersInfo(UsersInfoRequest request){
        String sql = "SELECT * FROM users WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UsersInfoResponse(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("password"),
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
}

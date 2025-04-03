package com.blog.domain.users.domain.repository;

import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.request.AuthKaKaoRequest;
import com.blog.domain.login.api.dto.request.LoginEmailRequest;
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
    public int addUsersByEmail(AuthEmailRequest request, String hashedPassword) {

        String sql = "INSERT INTO users (email, password, name, nickname, birth, profile_image, social, introduce) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                request.email(),
                hashedPassword,
                request.name(),
                request.nickname(),
                request.birth(),
                request.profileImage(),
                false,  // social: 기본값은 false
                request.introduce()
        );

        // 삽입된 레코드의 마지막 ID를 가져오는 쿼리
        String selectSql = "SELECT LAST_INSERT_ID()";

        return jdbcTemplate.queryForObject(selectSql, Integer.class);
    }

    // 이메일 중복확인
    public boolean isEmailDuplicated(String email) {

        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        return count != null && count > 0;
    }

    // 닉네임 중복확인
    public boolean isNickNameDuplicated(String nickname) {

        String sql = "SELECT COUNT(*) FROM users WHERE nickname = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nickname);

        return count != null && count > 0;
    }

    // 사용자 정보 수정
    public int usersUpdateInfo(UsersUpdateRequest request, String hashedPassword){
        String sql = "UPDATE users SET password = ?, nickname = ?, profile_image = ? WHERE id = ?";

        return jdbcTemplate.update(sql,
                hashedPassword,
                request.nickname(),
                request.profileImage(),
                request.userId()
        );
    }

    // 사용자 정보 조회
    public UsersInfoResponse getUsersByUserId(UsersIdRequest request){
        String sql = "SELECT * FROM users WHERE id = ?";

        try {

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UsersInfoResponse(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("nickname"),
                    rs.getObject("birth", LocalDate.class),
                    rs.getString("profileImage"),
                    rs.getBoolean("social"),
                    rs.getString("introduce")
            ), request.userId());
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }

    // 사용자 삭제
    public UsersResultResponse usersDeleteInfo(int userId){
        String sql = "DELETE FROM users WHERE id = ?";

        int result = jdbcTemplate.update(sql, userId);
        // 삭제 X
        if (result == 0) {
            return new UsersResultResponse(0);
        }

        return new UsersResultResponse(result);
    }


    // 로그인
    public Users getUsersByEmailAndPassword(LoginEmailRequest request, String hashedPassword){
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Users(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("nickname"),
                    rs.getString("password"),
                    rs.getString("profileImage"),
                    rs.getBoolean("social"),
                    rs.getString("introduce"),
                    rs.getObject("birth", LocalDate.class),
                    rs.getObject("created_at", LocalDateTime.class),
                    rs.getObject("updated_at", LocalDateTime.class)
            ), request.email(), hashedPassword);
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }

    // 카카오 회원가입
    public int addUserByKaKao(AuthKaKaoRequest request, String name) {

        String sql = "INSERT INTO users (email, password, name, nickname, birth, social, introduce) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                "kakao",
                "password",
                name,
                request.nickname(),
                request.birth(),
                true,  // social: 기본값은 false
                request.introduce()
        );

        // 삽입된 레코드의 마지막 ID를 가져오는 쿼리
        String selectSql = "SELECT LAST_INSERT_ID()";

        return jdbcTemplate.queryForObject(selectSql, Integer.class);
    }

    // 이메일, 이름으로 사용자 조회
    public Users getUsersByName (String name){
        String sql = "SELECT * FROM users WHERE name = ?";

        try {

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Users(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("nickname"),
                    rs.getString("password"),
                    rs.getString("profileImage"),
                    rs.getBoolean("social"),
                    rs.getString("introduce"),
                    rs.getObject("birth", LocalDate.class),
                    rs.getObject("created_at", LocalDateTime.class),
                    rs.getObject("updated_at", LocalDateTime.class)
            ), name);
        } catch (EmptyResultDataAccessException e) {

            return null;
        }
    }
}

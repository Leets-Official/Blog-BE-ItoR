package com.blog.domain.users.domain.repository;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
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
@Repository
public class UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsersRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 사용자 등록
    public int addUsersByEmail(Users user) {

        String sql = "INSERT INTO users (email, password, name, nickname, birth, profile_image, social, introduce) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getNickname(),
                user.getBirth(),
                user.getProfileImage(),
                user.isSocial(),
                user.getIntroduce()
        );

        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
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

            throw new CustomException(ErrorCode.NOT_FOUND_END_POINT);
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

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Users.fromResultSet(rs),
                    request.email(), hashedPassword);
        } catch (EmptyResultDataAccessException e) {

            throw new CustomException(ErrorCode.NOT_FOUND_END_POINT);
        }
    }

    // 카카오 회원가입
    public int addUserByKaKao(Users user) {

        String sql = "INSERT INTO users (email, password, name, nickname, birth, social, introduce) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getNickname(),
                user.getBirth(),
                user.isSocial(),
                user.getIntroduce()
        );

        // 삽입된 레코드의 마지막 ID를 가져오는 쿼리
        String selectSql = "SELECT LAST_INSERT_ID()";

        return jdbcTemplate.queryForObject(selectSql, Integer.class);
    }

    // 이메일, 이름으로 사용자 조회
    public Users getUsersByName (String name){
        String sql = "SELECT * FROM users WHERE name = ?";

        try {

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Users.fromResultSet(rs), name);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();

            throw new CustomException(ErrorCode.NOT_FOUND_END_POINT);
        }
    }

    // 닉네임 반환
    public String getUserNicknameByUserId(int userId){
        String sql = "SELECT nickname FROM users WHERE id = ?";

        return jdbcTemplate.queryForObject(sql,String.class, userId);
    }
}

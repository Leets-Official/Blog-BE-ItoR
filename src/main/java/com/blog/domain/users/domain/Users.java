package com.blog.domain.users.domain;

import com.blog.common.response.CustomException;
import com.blog.common.response.ErrorCode;
import com.blog.domain.auth.api.dto.request.AuthEmailRequest;
import com.blog.domain.auth.api.dto.request.AuthKaKaoRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Users {
    private int id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private LocalDate birth;
    private String profile_image;
    private Boolean social;
    private String introduce;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Users() {
    }

    // 이메일 회원가입
    public Users(String email, String password, String name, String nickname, LocalDate birth,
                 String profile_image, Boolean social, String introduce) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.profile_image = profile_image;
        this.social = social;
        this.introduce = introduce;
    }

    // 카카오 회원가입
    public Users(String name, String nickname, LocalDate birth, String introduce) {
        this.email = "kakao";
        this.password = "social";
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.profile_image = null;
        this.social = true;
        this.introduce = introduce;
    }

    // JDBC -> 도메인
    public Users(int id, String email, String name, String nickname, String password, String profile_image,
                 Boolean social, String introduce, LocalDate birth, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.profile_image = profile_image;
        this.social = social;
        this.introduce = introduce;
        this.birth = birth;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // 정적 팩토리 메소드 - 이메일 User
    public static Users createEmailUser(AuthEmailRequest request, String hashedPassword) {
        return new Users(
                request.email(),
                hashedPassword,
                request.name(),
                request.nickname(),
                request.birth(),
                request.profileImage(),
                false,
                request.introduce()
        );
    }

    // 정적 팩토리 메소드 - 카카오 User
    public static Users createKaKaoUser(AuthKaKaoRequest request, String name) {
        return new Users(
                name,
                request.nickname(),
                request.birth(),
                request.introduce()
        );
    }


    // 정적 팩토리 메소드 - User 담기
    public static Users fromResultSet(ResultSet rs) {
        try {
            return new Users(
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
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }
    }

    // Getter & Setter
    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeProfileImage(String profile_image) {
        this.profile_image = profile_image;
    }

    public void changeIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getUserId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public String getProfileImage() {
        return profile_image;
    }

    public boolean isSocial() {
        return social;
    }

    public String getIntroduce() {
        return introduce;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public LocalDateTime getUpdatedAt() {
        return updated_at;
    }
}

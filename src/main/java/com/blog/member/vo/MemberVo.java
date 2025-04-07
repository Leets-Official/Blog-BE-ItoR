package com.blog.member.vo;

import java.sql.Date;
import java.sql.Timestamp;

public class MemberVo {

    private String id;               // 아이디 (PK)
    private String email;            // 이메일
    private String pwd;              // 비밀번호
    private String nick;         // 닉네임
    private String bio;              // 한줄 소개
    private String name;             // 이름
    private Date birthdate;          // 생년월일
    private Timestamp createdAt;     // 가입일
    private boolean isActive;          // 탈퇴 여부 (boolean은 isActive()로 getter 필수)

    public MemberVo() {}

    @Override
    public String toString() {
        return "MemberVo{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", pwd='" + pwd + '\'' +
                ", nickname='" + nick + '\'' +
                ", bio='" + bio + '\'' +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                '}';
    }



    public MemberVo(String id,
                    String email,
                    String pwd,
                    String nick,
                    String bio,
                    String name,
                    Date birthdate,
                    Timestamp createdAt,
                    boolean isActive) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
        this.nick = nick;
        this.bio = bio;
        this.name = name;
        this.birthdate = birthdate;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}//class

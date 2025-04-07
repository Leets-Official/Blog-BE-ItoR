package com.blog.oauth.vo;

import java.sql.Timestamp;

public class OauthVo {

    private String id;
    private String memberId;
    private String provider;
    private String providerUserId;
    private String email;
    private String nick;

    public OauthVo() {

    }

    public OauthVo(String id,
                   String memberId,
                   String provider,
                   String providerUserId,
                   String email,
                   String nick) {
        this.id = id;
        this.memberId = memberId;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.email = email;
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "OauthVo{" +
                "id='" + id + '\'' +
                ", memberId='" + memberId + '\'' +
                ", provider='" + provider + '\'' +
                ", providerUserId='" + providerUserId + '\'' +
                ", email='" + email + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}//class

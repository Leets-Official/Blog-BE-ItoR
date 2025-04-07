package com.blog.kakaologin.service;

import com.blog.kakaologin.util.KakaoLoginUtil;
import com.blog.member.service.MemberService;
import com.blog.member.vo.MemberVo;
import com.blog.oauth.service.OauthService;
import com.blog.oauth.vo.OauthVo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class KakaoLoginService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private OauthService oauthService;

    public String kakaoLogin(String code) {
        try {
            String accessToken = getAccessTokenFromKakao(code);
            JSONObject userInfo = getUserInfoFromKakao(accessToken);
            MemberVo member = processOauthLogin(userInfo);
            return "로그인 성공! 닉네임: " + member.getNick() + ", 이메일: " + member.getEmail();
        } catch (Exception e) {
            e.printStackTrace();
            return "로그인 실패: " + e.getMessage();
        }
    }

    private String getAccessTokenFromKakao(String code) throws Exception {
        return KakaoLoginUtil.getAccessToken(code);
    }

    private JSONObject getUserInfoFromKakao(String accessToken) throws Exception {
        return KakaoLoginUtil.getUserInfo(accessToken);
    }

    private MemberVo processOauthLogin(JSONObject userInfo) {
        String email = userInfo.getString("email");
        String nickname = userInfo.getString("nickname");
        String kakaoId = String.valueOf(userInfo.getLong("id"));

        OauthVo oauth = oauthService.findByOauth("kakao", kakaoId);
        MemberVo member;

        if (oauth != null) {
            // 이미 Oauth가 등록된 경우 → 기존 회원으로 로그인
            member = memberService.findById(oauth.getMemberId());
        } else {
            // Oauth가 없는 경우 → 이메일 중복 체크
            if (memberService.existsByEmail(email)) {
                // 기존 이메일 회원이 있다면 → Oauth만 연결하고 로그인
                member = memberService.findByEmail(email);
                insertOauthAccount(kakaoId, member.getId());
            } else {
                // 신규 가입 로직
                member = createNewMember(email, nickname);
                insertOauthAccount(kakaoId, member.getId());
            }
        }
        return member;
    }

    // tlsr
    private MemberVo createNewMember(String email, String nickname) {
        MemberVo member = new MemberVo();
        member.setId(UUID.randomUUID().toString().replace("-", ""));
        member.setEmail(email);
        member.setNick(nickname);
        member.setName(nickname);
        member.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        member.setActive(true);

        memberService.insertMember(member);
        return member;
    }

    private void insertOauthAccount(String kakaoId, String memberId) {
        OauthVo oauthVo = new OauthVo();
        oauthVo.setId(UUID.randomUUID().toString().replace("-", ""));
        oauthVo.setProvider("kakao");
        oauthVo.setProviderUserId(kakaoId);
        oauthVo.setMemberId(memberId);;

        oauthService.insertOauth(oauthVo);
    }


}//class

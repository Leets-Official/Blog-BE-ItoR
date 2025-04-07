package com.blog.member.controller;

import com.blog.member.service.MemberService;
import com.blog.member.vo.MemberVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("member")
public class MemberController {

    @Autowired
    private MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    //회원가입
    @PostMapping("join")
    public String join(@RequestBody MemberVo vo){
        int result = service.join(vo);
         if(result == 0){
             throw new RuntimeException("[M-00] 회원가입 실패");
         }

         return "redirect:/login";
    }

    //로그인
    @PostMapping("login")
    public String login(@RequestBody MemberVo vo, HttpSession session){
        MemberVo loginMember = service.login(vo);
        if(loginMember == null){
            throw new RuntimeException("[M-01] 이메일 또는 비밀번호가 올바르지 않음");
        }

        session.setAttribute("loginMember", loginMember);
        return "로그인 성공";
    }

    
    //로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "로그아웃 성공"; // 로그아웃 후 홈으로 리디렉션
    }


}//class

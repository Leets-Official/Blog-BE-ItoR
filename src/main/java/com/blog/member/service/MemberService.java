package com.blog.member.service;

import com.blog.member.mapper.MemberMapper;
import com.blog.member.vo.MemberVo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    private MemberMapper mapper;

    @Autowired
    private ApplicationContext context;




    @PostConstruct
    public void checkMapper() {
        String[] names = context.getBeanNamesForType(MemberMapper.class);
        System.out.println(">>> 등록된 MemberMapper 빈: " + Arrays.toString(names));
    }


    public int join(MemberVo vo) {
        mapper.insertMember(vo);
        return 1; // 성공 시 1 반환 (또는 원하는 처리 결과 반환)
    }


    public void insertMember(MemberVo vo){

        if (vo.getId() == null || vo.getId().isEmpty()) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            vo.setId(uuid);
            System.out.println(">> 생성된 ID: " + uuid);
        }

        System.out.println(">> insert 대상 VO = " + vo);
        mapper.insertMember(vo);

    }


    public MemberVo login(MemberVo vo) {
        // DB에서 이메일로 회원 조회
        MemberVo dbMember = mapper.selectByEmail(vo.getEmail());

        // 조회된 회원이 없거나 비밀번호 불일치 시 로그인 실패
        if (dbMember == null || !dbMember.getPwd().equals(vo.getPwd())) {
            return null;
        }

        // 로그인 성공 시 해당 회원 정보 반환
        return dbMember;
    }

/*
---------------------------------------------------------------------------
 */

    //회원 정보 조회
    public MemberVo findById(String memberId) {
        return mapper.selectById(memberId);
    }

    //회원 정보 조회
    public MemberVo findByEmail(String email) {
        return mapper.selectByEmail(email);
    }
    
    //이메일 중복 여부 확인
    public boolean existsByEmail(String email){
        return mapper.selectByEmail(email) != null;
    }



}//class

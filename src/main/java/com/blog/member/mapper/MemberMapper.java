package com.blog.member.mapper;

import com.blog.member.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper //마이바티스 매퍼 인터페이스 인식
public interface MemberMapper {

    //이메일로 회원 조회
    MemberVo selectByEmail(@Param("email") String email);

    //Id로 회원 조회
    MemberVo selectById(String id);

    //회원 정보 삽입
    void insertMember(MemberVo vo);



}//interface

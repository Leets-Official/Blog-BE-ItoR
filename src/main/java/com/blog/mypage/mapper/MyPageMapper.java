package com.blog.mypage.mapper;

import com.blog.mypage.record.MyInfoRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyPageMapper {

    //-----------조회-------------
    MyInfoRecord selectMyInfo(String memberId);

    String selectPassword(@Param("memberId") String memberId);

    //-----------수정-------------
    int updateMyInfo(
            @Param("memberId") String memberId,
            @Param("name")     String name,
            @Param("nickname") String nickname,
            @Param("intro")    String intro,
            @Param("birthday") String birthday
    );

    int updatePassword(
            @Param("memberId") String memberId,
            @Param("newPwd")    String newPwd
    );

    //-----------삭제-------------
    void deleteProfileImg(@Param("memberId") String memberId);

    //-----------삽입-------------
    void insertProfileImg(
            @Param("memberId") String memberId,
            @Param("url")      String url
    );





}//interface

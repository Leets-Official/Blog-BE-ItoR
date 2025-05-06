package com.blog.mypage.record;

public record PasswordChangeRecord(

        String oldPwd,
        String newPwd,
        String newPwdConfirm



) {}//record

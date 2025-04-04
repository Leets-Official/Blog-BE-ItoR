package com.blog.global.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    // 비밀번호 암호화
    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = messageDigest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    // 비밀번호 검증 (입력된 비밀번호가 저장된 비밀번호와 일치하는지 확인)
    public static boolean checkPassword(String plainPassword, String storedPassword) {
        try {
            String encryptedPassword = encryptPassword(plainPassword);
            return encryptedPassword.equals(storedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password encryption error", e);
        }
    }
}

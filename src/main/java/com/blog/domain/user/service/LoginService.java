package com.blog.domain.user.service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.global.auth.jwtUtil.JwtUtil;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Optional;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private static final String SALT = "random_salt_value";

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("[로그인 실패] 존재하지 않는 사용자입니다."));

        String hashedInputPassword = hashPassword(password);

        if (user.getPassword().equals(hashedInputPassword)) {
            return JwtUtil.generateToken(user.getId().toString(), user.getEmail());
        }

        throw new RuntimeException("[로그인 실패] 비밀번호가 일치하지 않습니다.");
    }

    private String hashPassword(String password) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }
}

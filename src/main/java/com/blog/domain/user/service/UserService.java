package com.blog.domain.user.service;

import com.blog.domain.user.domain.User;
import com.blog.domain.user.repository.UserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private static final String SALT = "random_salt_value";

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void registerUser(String nickname, String password, String email, String profileImageUrl) {
    String hashedPassword = hashPassword(password);
    User user = new User(UUID.randomUUID(), nickname, hashedPassword, email, profileImageUrl, LocalDateTime.now(), LocalDateTime.now());
    userRepository.save(user);
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

  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}

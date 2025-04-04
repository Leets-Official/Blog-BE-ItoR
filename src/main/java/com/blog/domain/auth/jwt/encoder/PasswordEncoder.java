package com.blog.domain.auth.jwt.encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
	public static String encrypt(String userEmail, String password) {
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), getSalt(userEmail), 85319, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

			byte[] hash = factory.generateSecret(spec).getEncoded();
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] getSalt(String password)
		throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		byte[] keyBytes = password.getBytes("UTF-8");

		return digest.digest(keyBytes);
	}
}

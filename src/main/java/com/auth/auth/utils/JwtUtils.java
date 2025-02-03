package com.auth.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtils {

    private final SecretKey secretKey;

    public JwtUtils(@Value("${jwt.secret}") String secretKeyString) {
        this.secretKey = new SecretKeySpec(secretKeyString.getBytes(), "HmacSHA256");
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}

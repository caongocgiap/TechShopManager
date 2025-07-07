package com.techshop.server.util;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class GenerateClientUtils {

    public static String generateRandomClientSecret(int length) {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.out);
            return null;
        }
        byte[] buffer = new byte[length];
        secureRandom.nextBytes(buffer);
        return Base64.getEncoder().encodeToString(buffer);
    }

    public static String generateRandomClientID() {
        try {
            String originalString = UUID.randomUUID().toString() + System.currentTimeMillis();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static SecretKey generateJwtSecretKey(String clientId, String clientSecret) {
        String originalString = clientId + clientSecret;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 algorithm not available. Check your JDK configuration.", e);
        }
        byte[] hashed = md.digest(originalString.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(hashed);
    }

}

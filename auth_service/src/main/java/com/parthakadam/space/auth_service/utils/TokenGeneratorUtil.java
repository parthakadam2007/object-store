package com.parthakadam.space.auth_service.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGeneratorUtil {
    public static String generateSecureToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        // Base64 encoding (URL-safe version)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
}

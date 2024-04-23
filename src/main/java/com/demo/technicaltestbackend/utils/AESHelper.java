package com.demo.technicaltestbackend.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESHelper {
    private static final String ALGORITHM = "AES";

    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private static SecretKeySpec generateKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String encrypt(String plainText, String secretKey) throws Exception {
        SecretKeySpec keySpec = generateKey(secretKey);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, String secretKey) throws Exception {
        SecretKeySpec keySpec = generateKey(secretKey);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}

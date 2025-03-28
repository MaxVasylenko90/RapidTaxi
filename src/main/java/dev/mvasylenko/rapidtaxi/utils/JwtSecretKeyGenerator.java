package dev.mvasylenko.rapidtaxi.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static final String ALGORITHM = "HmacSHA256";

    public static void main(String[] args) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Secret Key: " + encodedKey);
    }
}

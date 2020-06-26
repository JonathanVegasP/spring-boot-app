package com.vegasdevelopments.app.repositories;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class AesRepository {
    private static byte[] key;

    private AesRepository() {

    }

    public static void init(String db) {
        try {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256, new SecureRandom(MD5Repository.encode(db).getBytes(StandardCharsets.UTF_8)));
            key = keyGenerator.generateKey().getEncoded();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public static void dispose() {
        key = null;
    }

    public static String encodeWithoutDecoder(String input) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return new HexBinaryAdapter().marshal(cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)));
    }

    public static String encode(String input) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(key, 16));
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new HexBinaryAdapter().marshal(cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decode(String input) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(key, 16));
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(new HexBinaryAdapter().unmarshal(input)), StandardCharsets.UTF_8);
    }
}

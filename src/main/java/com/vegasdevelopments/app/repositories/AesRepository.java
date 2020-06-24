package com.vegasdevelopments.app.repositories;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class AesRepository {
    private byte[] key;

    public AesRepository(String key) {
        init(key);
    }

    private void init(String db) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
            final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            digest.update(db.getBytes(StandardCharsets.UTF_8));
            keyGenerator.init(256, new SecureRandom(hexBinaryAdapter.marshal(digest.digest()).getBytes(StandardCharsets.UTF_8)));
            key = keyGenerator.generateKey().getEncoded();
        }catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public  String encode(String input) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(key, 32), "AES");
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(key, 16));
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes(StandardCharsets.UTF_8)));
    }

    public  String decode(String input) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(key, 32), "AES");
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(key, 16));
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(input)), StandardCharsets.UTF_8);
    }
}

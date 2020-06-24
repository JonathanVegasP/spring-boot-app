package com.vegasdevelopments.app.repositories;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Repository {
    private MD5Repository() {}

    public static String encode(String input) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(input.getBytes(StandardCharsets.UTF_8));
        return new HexBinaryAdapter().marshal(digest.digest());
    }
}

package org.loktevik.netcracker.security.utils;

public class SecretProvider {
    private static String secret = "secret";

    public static String getSecret() {
        return secret;
    }
}

package com.auth.rbacauth.security;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final int MAX_AGE = 15 * 60; // 15 minutes

    public String createJwtCookieHeader(String token, boolean secure) {
        String encoded = URLEncoder.encode(token, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();

        sb.append(ACCESS_TOKEN).append("=").append(encoded)
                .append("; Path=/")
                .append("; HttpOnly")
                .append("; Max-Age=").append(MAX_AGE)
                .append("; SameSite=Lax");

        if (secure) {
            sb.append("; Secure");
        }

        return sb.toString();
    }

    public String clearJwtSetCookieHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append(ACCESS_TOKEN).append("=; Path=/")
                .append("; HttpOnly")
                .append(";Max-Age=0")
                .append("; SameSite=Lax");

        return sb.toString();
    }

    public String cookieName() {
        return ACCESS_TOKEN;
    }

}

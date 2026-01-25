package com.auth.rbacauth.security;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${cookie.accessToken}")
    private String ACCESS_TOKEN;

    public ResponseCookie jwtCookie(String token) {
        return ResponseCookie.from(ACCESS_TOKEN, token)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofMinutes(15))
                .build();
    }

    public ResponseCookie clear() {
        return ResponseCookie.from(ACCESS_TOKEN, "")
                .path("/")
                .maxAge(0)
                .build();
    }
}

package com.auth.rbacauth.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.auth.rbacauth.models.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.token}")
    private String key;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generate(UserEntity user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }

    public Jwt decode(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new Jwt(
                token,
                claims.getIssuedAt().toInstant(),
                claims.getExpiration().toInstant(),
                Map.of("alg", "HS256"),
                claims);
    }

}

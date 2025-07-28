package com.example.tennismate.infrastructure.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {
    private final Key secretKey;
    private final Long accessTokenExpirationMs;
    private final Long refreshTokenExpirationMs;

    public JwtProvider(
            @Value("${jwt.secret}")
            String secretKey,
            @Value("${jwt.access-token-expiration-ms}")
            Long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms}")
            Long refreshTokenExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    /**
     * Access Token 생성
     * @param id : 사용자의 id PK 값
     * @param email : 사용자의 email 아이디
     * @param role : 사용자 권한
     * @return : Access Token
     */
    public String createAccessToken(Long id, String email, String role) {
        long now = System.currentTimeMillis();
        Date accessTokenExpiresIn = new Date(now + this.accessTokenExpirationMs);

        return Jwts.builder()
                .claims(Map.of("id", id, "email", email, "role", role))
                .issuedAt(new Date(now))
                .expiration(accessTokenExpiresIn)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Refresh Token 생성
     * @return : Refresh Token
     */
    public String createRefreshToken() {
        long now = System.currentTimeMillis();
        Date refreshTokenExpiresIn = new Date(now + this.refreshTokenExpirationMs);

        return Jwts.builder()
                .expiration(refreshTokenExpiresIn)
                .signWith(secretKey)
                .compact();
    }
}

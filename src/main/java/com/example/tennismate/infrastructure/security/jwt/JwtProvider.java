package com.example.tennismate.infrastructure.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {
    private final Key secretKey;
    private final Long accessTokenExpirationMs;
    private final Long refreshTokenExpirationMs;
    private final JwtParser jwtParser;

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
        this.jwtParser = Jwts.parser().verifyWith((SecretKey) this.secretKey).build();
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
     * @param email : 회원 ID 값
     * @return : Refresh Token
     */
    public String createRefreshToken(String email) {
        long now = System.currentTimeMillis();
        Date refreshTokenExpiresIn = new Date(now + this.refreshTokenExpirationMs);

        return Jwts.builder()
                .claims(Map.of("email", email))
                .expiration(refreshTokenExpiresIn)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰 유효성 검증
     * @param token : 검증할 토큰
     * @return : 유효하면 true
     */
    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT token : {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty : {}", e.getMessage());
        }
        return false;
    }

    /**
     * 토큰에서 사용자 정보(Claims) 추출
     * @param token : 정보를 추출할 토큰
     * @return 토큰에 담긴 정보
     */
    public Claims getUserInfoFromToken(String token) {
        // 미리 만들어둔 파서를 사용하여 토큰의 내용의 내용물인 Claims(payload) 를 추출
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    /**
     * 토큰에서 사용자 이메일 값 추출하는 메서드
     * @param token : JWT 토큰
     * @return : 사용자 이메일 값 반환
     */
    public String getUserEmailFromToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload().get("email", String.class);
    }

}

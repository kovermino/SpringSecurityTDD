package com.joel.springsecuritytdd.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class DefaultJwtGenerator implements JwtGenerator {

    private final long MINUTE = 60 * 1000L;
    private final long ACCESS_TOKEN_VALID_TIME = 30 * MINUTE;
    private final long REFRESH_TOKEN_VALID_TIME = 60 * MINUTE;

    @Value("${jwt.secret-key}")
    private String secretKey = "66409A951C506063DB6BEA51B01EFC7F1B8C27EFD671DFFE162B75FCD8F6BCFE";

    @Override
    public String createAccessToken(String email, List<String> authorities) {
        return createToken(email, authorities, ACCESS_TOKEN_VALID_TIME);
    }

    @Override
    public String createRefreshToken(String email, List<String> authorities) {
        return createToken(email, authorities, REFRESH_TOKEN_VALID_TIME);
    }

    private String createToken(String email, List<String> authorities, long validTime) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", authorities);
        Date now = new Date();
        return Jwts.builder()
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

package com.joel.springsecuritytdd.auth.token;

import com.google.gson.Gson;
import com.joel.springsecuritytdd.auth.domain.UserModel;
import com.joel.springsecuritytdd.auth.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultJwtGeneratorTest {

    private DefaultJwtGenerator sut;
    private String secretKey;
    private byte[] secretKeyBytes;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        secretKey = "66409A951C506063DB6BEA51B01EFC7F1B8C27EFD671DFFE162B75FCD8F6BCFE";
        secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        userModel = new UserModel("sample@sample.com", "samplePassword", Collections.singletonList(UserRole.ROLE_PLAIN.name()));
        sut = new DefaultJwtGenerator();
    }

    @Test
    void createAccessToken_secretKey로_signing된_accessToken을_생성한다() {
        String accessToken = sut.createAccessToken(userModel.getEmail(), null);


        assertThrows(UnsupportedJwtException.class, () -> {
            Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(accessToken)
                    .getBody()
                    .getSubject();
        });
    }

    @Test
    void createAccessToken_이메일을_사용해서_accessToken을_생성한다() {
        String accessToken = sut.createAccessToken(userModel.getEmail(), null);


        String subject = Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();


        assertEquals(userModel.getEmail(), subject);
    }

    @Test
    void createAccessToken_발행일자를_사용해서_accessToken을_생성한다() {
        String accessToken = sut.createAccessToken(userModel.getEmail(), null);


        Date issuedAt = Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getIssuedAt();


        assertNotNull(issuedAt);
    }

    @Test
    void createAccessToken_만료시간이_30분인_accessToken을_생성한다() {
        String accessToken = sut.createAccessToken(userModel.getEmail(), null);


        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        long gap = expiration.getTime() - issuedAt.getTime();
        assertTrue(gap == 30 * 60 * 1000L);
    }

    @Test
    void createAccessToken_UserRole을_가지고_있는_accessToken을_생성한다() {
        String accessToken = sut.createAccessToken(userModel.getEmail(), userModel.getRoles());


        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        List<String> roles = new Gson().fromJson(claims.get("roles").toString(), List.class);
        assertEquals(1, roles.size());
        assertEquals(UserRole.ROLE_PLAIN.name(), roles.get(0));
    }

    @Test
    void createRefreshToken_secretKey로_signing된_refreshToken을_생성한다() {
        String refreshToken = sut.createRefreshToken(userModel.getEmail(), null);


        assertThrows(UnsupportedJwtException.class, () -> {
            Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(refreshToken)
                    .getBody()
                    .getSubject();
        });
    }

    @Test
    void createRefreshToken_이메일을_사용해서_refreshToken을_생성한다() {
        String refreshToken = sut.createRefreshToken(userModel.getEmail(), null);


        String subject = Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();


        assertEquals(userModel.getEmail(), subject);
    }

    @Test
    void createRefreshToken_발행일자를_사용해서_refreshToken을_생성한다() {
        String refreshToken = sut.createRefreshToken(userModel.getEmail(), null);


        Date issuedAt = Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getIssuedAt();


        assertNotNull(issuedAt);
    }

    @Test
    void createRefreshToken_만료시간이_1시간인_refreshToken을_생성한다() {
        String refreshToken = sut.createRefreshToken(userModel.getEmail(), null);


        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        long gap = expiration.getTime() - issuedAt.getTime();
        assertTrue(gap == 60 * 60 * 1000L);
    }

    @Test
    void createRefreshToken_UserRole을_가지고_있는_refreshToken을_생성한다() {
        String refreshToken = sut.createRefreshToken(userModel.getEmail(), userModel.getRoles());


        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        List<String> roles = new Gson().fromJson(claims.get("roles").toString(), List.class);
        assertEquals(1, roles.size());
        assertEquals(UserRole.ROLE_PLAIN.name(), roles.get(0));
    }
}
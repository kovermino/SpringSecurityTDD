package com.joel.springsecuritytdd.auth.helper;

import com.joel.springsecuritytdd.auth.token.JwtGenerator;

import java.util.List;

public class StubJwtGenerator implements JwtGenerator {
    @Override
    public String createAccessToken(String email, List<String> roles) {
        return "someAccessToken";
    }

    @Override
    public String createRefreshToken(String email, List<String> roles) {
        return "someRefreshToken";
    }
}

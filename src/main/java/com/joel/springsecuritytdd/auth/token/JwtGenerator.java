package com.joel.springsecuritytdd.auth.token;

import java.util.List;

public interface JwtGenerator {
    String createAccessToken(String email, List<String> roles);
    String createRefreshToken(String email, List<String> roles);
}

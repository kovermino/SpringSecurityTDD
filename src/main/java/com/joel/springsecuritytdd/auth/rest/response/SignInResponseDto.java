package com.joel.springsecuritytdd.auth.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignInResponseDto {
    private String email;
    private String accessToken;
    private String refreshToken;
}

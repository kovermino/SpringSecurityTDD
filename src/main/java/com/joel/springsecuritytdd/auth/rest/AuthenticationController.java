package com.joel.springsecuritytdd.auth;

import com.joel.springsecuritytdd.auth.request.SignInRequestDto;
import com.joel.springsecuritytdd.auth.request.SignupRequestDto;
import com.joel.springsecuritytdd.auth.response.SignInResponseDto;
import com.joel.springsecuritytdd.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(path = "/signup")
    public String signup(@RequestBody SignupRequestDto request) {
        userService.createUser(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        return request.getEmail();
    }

    @PostMapping(path = "/signIn")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(token);
        return new SignInResponseDto(request.getEmail(), "accessToken", "refreshToken");
    }
}

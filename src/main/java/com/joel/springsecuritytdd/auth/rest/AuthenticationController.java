package com.joel.springsecuritytdd.auth.rest;

import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.auth.rest.request.SignInRequestDto;
import com.joel.springsecuritytdd.auth.rest.request.SignupRequestDto;
import com.joel.springsecuritytdd.auth.rest.response.SignInResponseDto;
import com.joel.springsecuritytdd.auth.token.JwtGenerator;
import com.joel.springsecuritytdd.user.dto.UserDto;
import com.joel.springsecuritytdd.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    @PostMapping(path = "/signup")
    public String signup(@RequestBody SignupRequestDto request) {
        UserDto user = UserDto.builder()
                .email(request.getEmail())
                .encryptedPassword(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(UserRole.ROLE_PLAIN))
                .build();
        userService.addUser(user);
        return request.getEmail();
    }

    @PostMapping(path = "/signIn")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        List<String> authorities = authentication.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toList());
        return SignInResponseDto.builder()
                .email(request.getEmail())
                .accessToken(jwtGenerator.createAccessToken(request.getEmail(), authorities))
                .refreshToken(jwtGenerator.createRefreshToken(request.getEmail(), authorities))
                .build();
    }
}

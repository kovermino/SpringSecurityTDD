package com.joel.springsecuritytdd.auth.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.auth.mock.SpyUserService;
import com.joel.springsecuritytdd.auth.mock.StubJwtGenerator;
import com.joel.springsecuritytdd.auth.rest.request.SignInRequestDto;
import com.joel.springsecuritytdd.auth.rest.request.SignupRequestDto;
import com.joel.springsecuritytdd.auth.token.JwtGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {

    private MockMvc sut;
    private SpyUserService userService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;
    private SignupRequestDto signupRequest;
    private SignInRequestDto signInRequest;
    private ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        userService = new SpyUserService();
        authenticationManager = mock(AuthenticationManager.class);
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        jwtGenerator = new StubJwtGenerator();
        signupRequest = new SignupRequestDto("sample@sample.com", "samplePassword");
        signInRequest = new SignInRequestDto("sample@sample.com", "samplePassword");


        sut = MockMvcBuilders.standaloneSetup(new AuthenticationController(userService, authenticationManager, passwordEncoder, jwtGenerator)).build();
    }

    @Test
    void 회원가입에_성공하면_회원이메일이_응답된다() throws Exception {
        String email = sut.perform(post("/api/signup")
                        .content(mapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        Assertions.assertEquals(signupRequest.getEmail(), email);
    }

    @Test
    void 회원가입을_하면_암호화된_비밀번호가_저장된다() throws Exception {
        sut.perform(post("/api/signup")
                        .content(mapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        Assertions.assertNotEquals(signupRequest.getPassword(), userService.receivedPassword);
        Assertions.assertTrue(passwordEncoder.matches(signupRequest.getPassword(), userService.receivedPassword));
    }

    @Test
    void 로그인에_성공하면_토큰응답을_받아야_한다() throws Exception {
        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword(), Collections.singletonList(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return UserRole.ROLE_PLAIN.toString();
                    }
                }))
        );
        sut.perform(post("/api/signIn")
                .content(mapper.writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(jwtGenerator.createAccessToken(signInRequest.getEmail(), Collections.singletonList(UserRole.ROLE_PLAIN.toString()))))
                .andExpect(jsonPath("$.refreshToken").value(jwtGenerator.createRefreshToken(signInRequest.getEmail(), Collections.singletonList(UserRole.ROLE_PLAIN.toString()))));
    }
}
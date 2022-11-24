package com.joel.springsecuritytdd.auth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.auth.rest.request.SignInRequestDto;
import com.joel.springsecuritytdd.auth.rest.request.SignupRequestDto;
import com.joel.springsecuritytdd.user.entity.UserEntity;
import com.joel.springsecuritytdd.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc sut;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    private SignupRequestDto signupRequest;
    private SignInRequestDto signInRequest;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequestDto("sample@sample.com", "samplePassword");
        signInRequest = new SignInRequestDto("sample@sample.com", "samplePassword");
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
    void 로그인에_성공하면_토큰응답을_받아야_한다() throws Exception {
        UserEntity sampleUser = UserEntity.builder()
                .email(signInRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .roles(Set.of(UserRole.ROLE_ADMIN))
                .build();
        userRepository.save(sampleUser);

        sut.perform(post("/api/signIn")
                        .content(mapper.writeValueAsString(signInRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken", notNullValue()));
    }
}

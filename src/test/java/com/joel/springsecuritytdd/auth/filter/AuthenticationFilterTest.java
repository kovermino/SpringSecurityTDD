package com.joel.springsecuritytdd.auth.filter;

import com.joel.springsecuritytdd.auth.domain.UserAuthModel;
import com.joel.springsecuritytdd.auth.domain.UserRole;
import com.joel.springsecuritytdd.auth.token.DefaultJwtGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    private AuthenticationFilter sut;
    private UserAuthModel user;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private MockFilterChain mockFilterChain;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        user = new UserAuthModel("joel@sample.com", "samplePassword", Collections.singletonList(UserRole.ROLE_ADMIN.name()));
        userDetailsService = mock(UserDetailsService.class);
        sut = new AuthenticationFilter(userDetailsService);
        mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", getJwtToken(user.getEmail(), user.getRoles()));
        mockResponse = new MockHttpServletResponse();
        mockFilterChain = new MockFilterChain();
    }

    @Test
    void doFilterInternal_토큰에서_사용자_정보를_가져온다() throws ServletException, IOException {
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(user);


        sut.doFilter(mockRequest, mockResponse, mockFilterChain);


        verify(userDetailsService, times(1)).loadUserByUsername(user.getEmail());
    }

    @Test
    void doFilterInternal_토큰에서_가져온_사용자_정보를_SecurityContextHolder에_저장한다() throws ServletException, IOException {
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(user);


        sut.doFilter(mockRequest, mockResponse, mockFilterChain);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        assertEquals(user.getEmail(), principal.getUsername());
        assertEquals(user.getRoles(), principal.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()));
        assertEquals(authentication.getAuthorities(), principal.getAuthorities());
    }

    @Test
    void doFilterInternal_jwt를_디코딩하고_다음_필터로_작업을_넘긴다() throws ServletException, IOException {
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(user);
        MockFilterChain spyMockFilterChain = spy(mockFilterChain);


        sut.doFilter(mockRequest, mockResponse, spyMockFilterChain);


        verify(spyMockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    void doFilterInternal_jwt가_없는_경우에도_다음_필터로_작업을_넘긴다() throws ServletException, IOException {
        MockFilterChain spyMockFilterChain = spy(mockFilterChain);
        MockHttpServletRequest requestWithoutHeader = new MockHttpServletRequest();


        sut.doFilter(requestWithoutHeader, mockResponse, spyMockFilterChain);


        verify(spyMockFilterChain, times(1)).doFilter(requestWithoutHeader, mockResponse);
    }

    private String getJwtToken(String email, List<String> roles) {
        DefaultJwtGenerator tokenGenerator = new DefaultJwtGenerator();
        return "Bearer " + tokenGenerator.createAccessToken(email, roles);
    }
}
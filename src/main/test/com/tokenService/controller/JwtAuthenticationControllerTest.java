package com.tokenService.controller;

import com.tokenService.config.JwtTokenUtil;
import com.tokenService.model.JwtRequest;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtAuthenticationControllerTest {

    @Mock
    UserDetails userDetails;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtRequest authenticationRequest;

    @Mock
    Claims claims;

    @InjectMocks
    JwtAuthenticationController jwtAuthenticationController;

    @BeforeEach
    public void setup() {
        when(authenticationRequest.getUsername()).thenReturn("testuserName");
        when(authenticationRequest.getPassword()).thenReturn("testPassword");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn("testJWTToken");
    }


    @Test
    void createAuthenticationToken() {
        try {
            ResponseEntity<?> actualResult = jwtAuthenticationController.createAuthenticationToken(authenticationRequest);
            assertNotNull(actualResult);
            assertEquals(actualResult.getStatusCode(), HttpStatus.OK);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void validateToken() {
        when(jwtTokenUtil.getUsernameFromToken(anyString())).thenReturn("testUserName");
        when(jwtTokenUtil.validateToken(anyString(), any(UserDetails.class))).thenReturn(true);
        String actualResult = jwtAuthenticationController.validateToken("testJWTToken");
        assertNotNull(actualResult);
        assertEquals(actualResult, "testUserName");
    }

    @Test
    void getClaims() {
        when(jwtTokenUtil.getAllClaimsFromToken(anyString())).thenReturn(claims);
        when(claims.get(eq("role"))).thenReturn("testRole");
        String actualResult = jwtAuthenticationController.getClaims("testJWTToken");
        assertNotNull(actualResult);
    }
}
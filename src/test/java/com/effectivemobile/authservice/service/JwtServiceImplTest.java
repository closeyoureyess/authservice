package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.AbstractContainerTest;
import com.effectivemobile.authservice.service.security.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest extends AbstractContainerTest {

    private final String testEmail = "test@example.com";
    @InjectMocks
    private JwtServiceImpl jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = User.builder()
                .username(testEmail)
                .password("")
                .roles("USER")
                .build();
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void extractEmailUser_ShouldReturnCorrectEmail() {
        String token = jwtService.generateToken(userDetails);
        String email = jwtService.extractEmailUser(token);

        assertEquals(testEmail, email);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token));
    }
}

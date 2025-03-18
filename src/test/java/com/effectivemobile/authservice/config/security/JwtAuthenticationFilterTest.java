package com.effectivemobile.authservice.config.security;

import com.effectivemobile.authservice.AbstractContainerTest;
import com.effectivemobile.authservice.service.security.JwtService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest extends AbstractContainerTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.setJwtService(jwtService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAuthenticationWhenValidToken() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer valid.token.here");


        Mockito.when(jwtService.extractEmailUser("valid.token.here")).thenReturn("test@example.com");
        Mockito.when(jwtService.isTokenValid("valid.token.here")).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("test@example.com", SecurityContextHolder.getContext().getAuthentication().getName());
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenInvalidHeader() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "InvalidHeader");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenNoToken() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldHandleInvalidTokenGracefully() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalid.token");

        Mockito.when(jwtService.extractEmailUser("invalid.token")).thenReturn("test@example.com");
        Mockito.when(jwtService.isTokenValid("invalid.token")).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain).doFilter(request, response);
    }
}

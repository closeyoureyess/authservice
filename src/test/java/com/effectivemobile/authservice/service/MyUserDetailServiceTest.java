package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.AbstractContainerTest;
import com.effectivemobile.authservice.exceptions.UserNotFoundException;
import com.effectivemobile.authservice.service.security.MyUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MyUserDetailServiceTest extends AbstractContainerTest {

    private final String testEmail = "test@example.com";
    @Mock
    private RedisTemplate<Object, Object> redisTemplate;
    @Mock
    private ValueOperations<Object, Object> valueOperations;
    @InjectMocks
    private MyUserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails() {
        when(valueOperations.get(testEmail)).thenReturn(testEmail);

        UserDetails userDetails = userDetailService.loadUserByUsername(testEmail);

        assertNotNull(userDetails);
        assertEquals(testEmail, userDetails.getUsername());
        verify(valueOperations).get(testEmail);
    }

    @Test
    void loadUserByUsername_ShouldThrowUserNotFoundException() {
        when(valueOperations.get(testEmail)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userDetailService.loadUserByUsername(testEmail);
        });

        verify(valueOperations).get(testEmail);
    }
}

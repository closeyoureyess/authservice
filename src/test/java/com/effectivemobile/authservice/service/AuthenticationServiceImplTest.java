package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.AbstractContainerTest;
import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.other.TokenSuccessfullyValidEvent;
import com.effectivemobile.authservice.service.kafka.KafkaSenderService;
import com.effectivemobile.authservice.service.security.JwtService;
import com.effectivemobile.authservice.service.security.MyUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest extends AbstractContainerTest {

    private final String testEmail = "test@example.com";
    @Mock
    private KafkaSenderService kafkaSenderService;
    @Mock
    private RedisTemplate<Object, Object> redisTemplate;
    @Mock
    private MyUserDetailService userDetailsService;
    @Mock
    private JwtService jwtService;
    @Mock
    private CompletableFuture<OneTimeTokenDto> futureToken;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBarrierToken_ShouldReturnJwtWhenValid() throws Exception {
        OneTimeTokenDto tokenDto = new OneTimeTokenDto();
        tokenDto.setEmail(testEmail);
        String expectedJwt = "test.jwt.token";

        when(futureToken.get(5, TimeUnit.SECONDS)).thenReturn(tokenDto);
        when(userDetailsService.loadUserByUsername(testEmail))
                .thenReturn(mock(UserDetails.class));
        when(jwtService.generateToken(any())).thenReturn(expectedJwt);

        Optional<String> result = authenticationService.getBarrierToken(new OneTimeTokenDto());

        assertTrue(result.isPresent());
        assertEquals(expectedJwt, result.get());
    }

    @Test
    void tokenIsValidEvent_ShouldCompleteFuture() {
        TokenSuccessfullyValidEvent event = new TokenSuccessfullyValidEvent(
                this, new OneTimeTokenDto());

        authenticationService.tokenIsValidEvent(event);

        verify(futureToken).complete(event.getOneTimeTokenDto());
    }
}
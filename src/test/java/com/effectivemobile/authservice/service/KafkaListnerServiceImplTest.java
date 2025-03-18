package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.AbstractContainerTest;
import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.other.TokenSuccessfullyValidEvent;
import com.effectivemobile.authservice.service.kafka.KafkaListnerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaListnerServiceImplTest extends AbstractContainerTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private KafkaListnerServiceImpl kafkaListnerService;

    private OneTimeTokenDto validToken;

    @BeforeEach
    void setUp() {
        validToken = new OneTimeTokenDto();
        validToken.setUsed(true);
    }

    @Test
    void listenTokenIsValid_ShouldPublishEventWhenValid() {
        kafkaListnerService.listenTokenIsValid(validToken);

        verify(eventPublisher).publishEvent(any(TokenSuccessfullyValidEvent.class));
    }

    @Test
    void listenTokenIsValid_ShouldNotPublishEventWhenInvalid() {
        OneTimeTokenDto invalidToken = new OneTimeTokenDto();
        invalidToken.setUsed(false);

        kafkaListnerService.listenTokenIsValid(invalidToken);

        verifyNoInteractions(eventPublisher);
    }
}

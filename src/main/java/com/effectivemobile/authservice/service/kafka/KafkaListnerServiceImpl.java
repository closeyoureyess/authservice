package com.effectivemobile.authservice.service.kafka;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.other.TokenSuccessfullyValidEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListnerServiceImpl implements KafkaListnerService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public KafkaListnerServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @KafkaListener(topics = "${kafka.consumer.topic-name.token-is-valid}", groupId = "auth-service-group-one")
    @Override
    public void listenTokenIsValid(OneTimeTokenDto oneTimeTokenDto) {
        boolean isValid = oneTimeTokenDto.getUsed();
        if (isValid) {
           applicationEventPublisher.publishEvent(new TokenSuccessfullyValidEvent(this, oneTimeTokenDto));
        }
    }
}

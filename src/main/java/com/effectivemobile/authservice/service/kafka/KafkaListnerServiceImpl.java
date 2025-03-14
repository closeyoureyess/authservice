package com.effectivemobile.authservice.service.kafka;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.service.AuthenticationService;
import com.effectivemobile.codegenerateservice.exeptions.TokenNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListnerServiceImpl implements KafkaListnerService {

    private final AuthenticationService authenticationService;

    @Autowired
    public KafkaListnerServiceImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @KafkaListener(topics = "${kafka.topic-name.objectTokenWasUsed}")
    @Override
    public void listenoObjectTokenWasUsed(OneTimeTokenDto oneTimeTokenDto) {
        if (oneTimeTokenDto != null) {
            authenticationService.getBarrierToken(oneTimeTokenDto);
        }
    }

    @KafkaListener(topics = "${kafka.topic-name.tokenObject}")
    @Override
    public void listenToken(OneTimeTokenDto oneTimeTokenDto) {
        if (oneTimeTokenDto != null) {
            authenticationService.signIn(oneTimeTokenDto);
        }
    }
}

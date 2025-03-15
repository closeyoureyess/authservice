package com.effectivemobile.authservice.service.kafka;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;

public interface KafkaListnerService {

    void listenTokenIsValid(OneTimeTokenDto oneTimeTokenDto);

}

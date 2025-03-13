package com.effectivemobile.authservice.service.kafka;

import com.effectivemobile.codegenerateservice.exeptions.KafkaSenderRuntimeException;

public interface KafkaSenderService {

    void sendToTopic(String topic, Object message) throws KafkaSenderRuntimeException;

}

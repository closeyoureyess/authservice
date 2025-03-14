package com.effectivemobile.authservice.service.kafka;

import com.effectivemobile.authservice.exceptions.KafkaSenderRuntimeException;

public interface KafkaSenderService {

    void sendToTopic(String topic, Object message) throws KafkaSenderRuntimeException;

}

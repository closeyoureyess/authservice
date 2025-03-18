package com.effectivemobile.authservice.service.kafka;

import com.effectivemobile.authservice.exceptions.KafkaSenderRuntimeException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static com.effectivemobile.authservice.exceptions.ExceptionsDescription.TOPIC_OR_OBJECT_IN_KAFKA_IS_INCORRECT;
import static com.effectivemobile.authservice.other.ConstantsClass.KAFKA_PRODUCER_TRUST_CUSTOMUSER;
import static com.effectivemobile.authservice.other.ConstantsClass.KAFKA_PRODUCER_TRUST_ONETIMETOKEN;

@Service
@Slf4j
public class KafkaSenderServiceImpl implements KafkaSenderService {

    @Value("${kafka.producer.topic-name.object-email-address}")
    @Setter
    private String sendObjectEmailAddressTopicName;

    @Value("${kafka.producer.topic-name.object-token}")
    @Setter
    private String sendObjectTokenTopicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaSenderServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToTopic(String topic, Object message) throws KafkaSenderRuntimeException {
        String key;
        if (topic.equals(sendObjectEmailAddressTopicName)) {
            topic = sendObjectEmailAddressTopicName;
            key = KAFKA_PRODUCER_TRUST_CUSTOMUSER;
        } else if (topic.equals(sendObjectTokenTopicName)) {
            topic = sendObjectTokenTopicName;
            key = KAFKA_PRODUCER_TRUST_ONETIMETOKEN;
        } else {
            throw new KafkaSenderRuntimeException(TOPIC_OR_OBJECT_IN_KAFKA_IS_INCORRECT.getDescription());
        }

        Message<Object> kafkaMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(JsonSerializer.TYPE_MAPPINGS, key)
                .build();

        kafkaTemplate.send(kafkaMessage);
    }
}

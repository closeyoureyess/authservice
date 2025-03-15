package com.effectivemobile.authservice.service.kafka;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.exceptions.KafkaSenderRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.effectivemobile.authservice.exceptions.ExceptionsDescription.TOPIC_OR_OBJECT_IN_KAFKA_IS_INCORRECT;

@Service
public class KafkaSenderServiceImpl implements KafkaSenderService {

    @Value("${kafka.producer.topic-name.object-email-address}")
    private String sendObjectEmailAddressTopicName;

    @Value("${kafka.producer.topic-name.object-token}")
    private String sendObjectTokenTopicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaSenderServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToTopic(String topic, Object message) throws KafkaSenderRuntimeException {
        topic = qualifyTopic(topic, message);
        kafkaTemplate.send(topic, message);
    }

    private String qualifyTopic(String topic, Object message) throws KafkaSenderRuntimeException {
        if (message instanceof OneTimeTokenDto) {
            if (topic.equals(sendObjectEmailAddressTopicName)) {
                topic = sendObjectEmailAddressTopicName;
            } else if (topic.equals(sendObjectTokenTopicName)) {
                topic = sendObjectTokenTopicName;
            } else {
                throw new KafkaSenderRuntimeException(TOPIC_OR_OBJECT_IN_KAFKA_IS_INCORRECT.getDescription());
            }
        }
        return topic;
    }
}

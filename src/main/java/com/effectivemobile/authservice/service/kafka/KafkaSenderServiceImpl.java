package com.effectivemobile.authservice.service.kafka;

import com.effectivemobile.authservice.exceptions.KafkaSenderRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.effectivemobile.authservice.exceptions.ExceptionsDescription.TOPIC_OR_OBJECT_IN_KAFKA_IS_INCORRECT;

@Service
public class KafkaSenderServiceImpl implements KafkaSenderService {

    @Value("${kafka.topic-name.objectEmail}")
    private final String objectEmailTopicName;

    @Value("${kafka.topic-name.objectTokenWasUsed}")
    private final String objectTokenWasUsedTopicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaSenderServiceImpl(String objectEmailTopicName, String objectTokenWasUsedTopicName,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
        this.objectEmailTopicName = objectEmailTopicName;
        this.objectTokenWasUsedTopicName = objectTokenWasUsedTopicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToTopic(String topic, Object message) throws KafkaSenderRuntimeException {
        topic = qualifyTopic(topic, message);
        kafkaTemplate.send(topic, message);
    }

    private String qualifyTopic(String topic, Object message) throws KafkaSenderRuntimeException {
        if (topic.equals(objectEmailTopicName) && message instanceof String) {
            topic = objectEmailTopicName;
        } else if (topic.equals(objectTokenWasUsedTopicName) && message instanceof Boolean) {
            topic = objectTokenWasUsedTopicName;
        } else {
            throw new KafkaSenderRuntimeException(TOPIC_OR_OBJECT_IN_KAFKA_IS_INCORRECT.getDescription());
        }
        return topic;
    }
}

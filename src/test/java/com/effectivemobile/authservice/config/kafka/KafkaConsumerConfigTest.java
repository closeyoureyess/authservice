package com.effectivemobile.authservice.config.kafka;

import com.effectivemobile.authservice.AbstractContainerTest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class KafkaConsumerConfigTest extends AbstractContainerTest {

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    @Autowired
    private ConsumerFactory<String, Object> consumerFactory;

    @Test
    void consumerFactoryShouldBeProperlyConfigured() {
        assertNotNull(consumerFactory);
        assertEquals(
                StringDeserializer.class,
                kafkaConsumerConfig.consumerConfigs().get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG)
        );
    }
}
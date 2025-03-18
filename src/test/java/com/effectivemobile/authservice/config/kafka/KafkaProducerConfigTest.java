package com.effectivemobile.authservice.config.kafka;

import com.effectivemobile.authservice.AbstractContainerTest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KafkaProducerConfigTest extends AbstractContainerTest {

    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void producerConfigsShouldContainCorrectProperties() {
        Map<String, Object> props = kafkaProducerConfig.producerConfigs();

        assertAll(
                () -> assertNotNull(props.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG)),
                () -> assertEquals(StringSerializer.class, props.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)),
                () -> assertEquals(JsonSerializer.class, props.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)),
                () -> assertTrue(((String) props.get(JsonSerializer.TYPE_MAPPINGS)).contains("oneTimeToken"))
        );
    }

    @Test
    void kafkaTemplateShouldBeAvailable() {
        assertNotNull(kafkaTemplate);
    }
}


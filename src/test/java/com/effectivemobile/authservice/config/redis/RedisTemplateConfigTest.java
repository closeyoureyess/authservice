package com.effectivemobile.authservice.config.redis;

import com.effectivemobile.authservice.AbstractContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
class RedisTemplateConfigTest extends AbstractContainerTest {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    void redisTemplateShouldBeConfiguredWithCorrectSerializers() {
        assertAll(
                () -> assertInstanceOf(StringRedisSerializer.class, redisTemplate.getKeySerializer()),
                () -> assertInstanceOf(GenericJackson2JsonRedisSerializer.class, redisTemplate.getValueSerializer()),
                () -> assertInstanceOf(StringRedisSerializer.class, redisTemplate.getHashKeySerializer()),
                () -> assertInstanceOf(GenericJackson2JsonRedisSerializer.class, redisTemplate.getHashValueSerializer())
        );
    }
}

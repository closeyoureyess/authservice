package com.effectivemobile.authservice.other;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class InfoMessagesHandler {
    @Before("@annotation(kafkaListener)")
    public void logBefore(JoinPoint joinPoint, KafkaListener kafkaListener) {
        Object[] args = joinPoint.getArgs();
        log.info("📩 Получено сообщение из Kafka: Topic = {}, Аргументы = {}",
                kafkaListener.topics(), args);
    }
}

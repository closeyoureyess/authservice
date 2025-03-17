package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.entity.CustomUser;
import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.other.TokenSuccessfullyValidEvent;
import com.effectivemobile.authservice.service.kafka.KafkaSenderService;
import com.effectivemobile.authservice.service.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${kafka.producer.topic-name.object-email-address}")
    private String sendObjectEmailAddressTopicName;

    @Value("${kafka.producer.topic-name.object-token}")
    private String sendObjectTokenTopicName;

    private OneTimeTokenDto oneTimeTokenIsValidEvent;

    private final CompletableFuture<OneTimeTokenDto> futureToken;

    private final KafkaSenderService kafkaSenderService;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    @Autowired
    public AuthenticationServiceImpl(KafkaSenderService kafkaSenderService, RedisTemplate<Object, Object> redisTemplate,
                                     UserDetailsService userDetailsService, JwtService jwtService, CompletableFuture<OneTimeTokenDto> futureToken) {
        this.kafkaSenderService = kafkaSenderService;
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.futureToken = futureToken;
    }

    @EventListener
    @Override
    public void tokenIsValidEvent(TokenSuccessfullyValidEvent tokenSuccessfullyValidEvent) {
        futureToken.complete(tokenSuccessfullyValidEvent.getOneTimeTokenDto());
    }

    @Override
    public void signIn(CustomUser customUser) {
        String email = customUser.getEmail();
        redisTemplate.opsForValue().set(email, email);
        kafkaSenderService.sendToTopic(sendObjectEmailAddressTopicName, customUser);
    }

    @Override
    public Optional<String> getBarrierToken(OneTimeTokenDto oneTimeCode) throws ExecutionException, InterruptedException, TimeoutException {
        kafkaSenderService.sendToTopic(sendObjectTokenTopicName, oneTimeCode);
        oneTimeTokenIsValidEvent = futureToken.get(5, TimeUnit.SECONDS);
        if (this.oneTimeTokenIsValidEvent != null) {
            String userEmail = this.oneTimeTokenIsValidEvent.getEmail();
            log.info("User email from event: {}", userEmail);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            String jwtToken = jwtService.generateToken(userDetails);
            log.info("Token: {}", jwtToken);
            return Optional.of(jwtService.generateToken(userDetails));
        }
        log.info("Token is not valid!");
        return Optional.empty();
    }
}

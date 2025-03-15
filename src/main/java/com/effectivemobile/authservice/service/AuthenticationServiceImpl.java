package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.entity.CustomUser;
import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.other.TokenSuccessfullyValidEvent;
import com.effectivemobile.authservice.service.kafka.KafkaSenderService;
import com.effectivemobile.authservice.service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${kafka.producer.topic-name.object-email-address}")
    private String sendObjectEmailAddressTopicName;

    @Value("${kafka.producer.topic-name.object-token}")
    private String sendObjectTokenTopicName;

    private OneTimeTokenDto oneTimeTokenIsValidEvent;

    private final KafkaSenderService kafkaSenderService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    @Autowired
    public AuthenticationServiceImpl(KafkaSenderService kafkaSenderService, RedisTemplate<String, Object> redisTemplate,
                                     UserDetailsService userDetailsService, JwtService jwtService) {
        this.kafkaSenderService = kafkaSenderService;
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @EventListener
    @Override
    public void tokenIsValidEvent(TokenSuccessfullyValidEvent tokenSuccessfullyValidEvent) {
        OneTimeTokenDto oneTimeTokenDto = tokenSuccessfullyValidEvent.getOneTimeTokenDto();
        if (oneTimeTokenDto != null) {
            this.oneTimeTokenIsValidEvent = oneTimeTokenDto;
        }
    }

    @Override
    public void signIn(CustomUser customUser) {
        String email = customUser.getEmail();
        redisTemplate.opsForValue().set(email, email);
        kafkaSenderService.sendToTopic(sendObjectEmailAddressTopicName, customUser);
    }

    @Override
    public Optional<String> getBarrierToken(OneTimeTokenDto oneTimeCode) {
        kafkaSenderService.sendToTopic(sendObjectTokenTopicName, oneTimeCode);
        if (this.oneTimeTokenIsValidEvent != null) {
            String userEmail = this.oneTimeTokenIsValidEvent.getEmail();
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            return Optional.of(jwtService.generateToken(userDetails));
        }
        return Optional.empty();
    }
}

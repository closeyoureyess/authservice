package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.entity.UserAuthentication;
import com.effectivemobile.authservice.other.Message;
import com.effectivemobile.authservice.other.MessageDescription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.effectivemobile.authservice.other.MessageDescription.MESSAGE_IS_PENDING;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${kafka.topic-name.booleanVerifyToken}")
    private String booleanVerifyTokenTopicName;

    @Override
    public Message signIn(OneTimeTokenDto oneTimeTokenDto) {
        if (oneTimeTokenDto.getEmail() != null) {

        }
        return new Message(MESSAGE_IS_PENDING.getDescription());
    }

    @Override
    public OneTimeTokenDto getBarrierToken(OneTimeTokenDto oneTimeCode) {
        if (oneTimeCode.getUsed() != null && oneTimeCode.getUsed().equals(true)) {

        }
    }
}

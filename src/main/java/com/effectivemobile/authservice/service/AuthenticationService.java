package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.entity.UserAuthentication;
import com.effectivemobile.authservice.other.Message;

public interface AuthenticationService {

    Message signIn(OneTimeTokenDto oneTimeTokenDto);

    OneTimeTokenDto getBarrierToken(OneTimeTokenDto oneTimeTokenDto);
}

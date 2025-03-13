package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.entity.UserAuthentication;

public interface AuthenticationService {

    void signIn(UserAuthentication userAuthentication);

    void getBarrierToken(OneTimeTokenDto oneTimeCode);
}

package com.effectivemobile.authservice.service;

import com.effectivemobile.authservice.entity.CustomUser;
import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.other.TokenSuccessfullyValidEvent;
import com.effectivemobile.authservice.other.validationgroups.EmailObjectValidationGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface AuthenticationService {

    @Validated(EmailObjectValidationGroup.class)
    void signIn(@Valid CustomUser customUser);

    Optional<String> getBarrierToken(@Valid @NotNull(message = "Object don't be null") OneTimeTokenDto oneTimeTokenDto) throws ExecutionException, InterruptedException, TimeoutException;

    void tokenIsValidEvent(TokenSuccessfullyValidEvent tokenSuccessfullyValidEvent);
}

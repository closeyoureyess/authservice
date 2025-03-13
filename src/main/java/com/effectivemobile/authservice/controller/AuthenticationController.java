package com.effectivemobile.authservice.controller;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.entity.UserAuthentication;
import com.effectivemobile.authservice.other.Message;
import com.effectivemobile.authservice.other.MessageDescription;
import com.effectivemobile.authservice.other.validationgroups.GroupOne;
import com.effectivemobile.authservice.other.validationgroups.GroupTwo;
import com.effectivemobile.authservice.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jboss.logging.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @Autowired
    AuthenticationController(AuthenticationService authenticationService) {

    }

    @PostMapping("/api/sign-in")
    @Validated(GroupOne.class)
    public ResponseEntity<OneTimeTokenDto> signIn(@Valid @RequestBody @NotNull OneTimeTokenDto oneTimeTokenDto) {
        Message message = authenticationService.signIn(oneTimeTokenDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/")
    @Validated(GroupTwo.class)
    public ResponseEntity<OneTimeTokenDto> barrierToken(@Valid @RequestBody @NotBlank OneTimeTokenDto oneTimeTokenDto) {

        return ResponseEntity.ok().build();
    }
}

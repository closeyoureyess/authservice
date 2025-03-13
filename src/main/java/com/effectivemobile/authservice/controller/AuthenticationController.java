package com.effectivemobile.authservice.controller;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.entity.UserAuthentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @PostMapping("/api/sign-in")
    public ResponseEntity<OneTimeTokenDto> signIn(@Valid @RequestBody @NotBlank UserAuthentication userAuthentication) {

        return ResponseEntity.ok().build();
    }

}

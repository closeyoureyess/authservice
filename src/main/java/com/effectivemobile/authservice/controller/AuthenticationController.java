package com.effectivemobile.authservice.controller;

import com.effectivemobile.authservice.entity.CustomUser;
import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import com.effectivemobile.authservice.other.Message;
import com.effectivemobile.authservice.other.validationgroups.EmailObjectValidationGroup;
import com.effectivemobile.authservice.other.validationgroups.TokenObjectValidationGroups;
import com.effectivemobile.authservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.effectivemobile.authservice.other.MessageDescription.MESSAGE_IS_PENDING;

@RestController
@RequestMapping("/api")
@Validated
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @Autowired
    AuthenticationController(AuthenticationService authenticationService) {

    }

    @PostMapping("/api/otp-token")
    @Validated(EmailObjectValidationGroup.class)
    public ResponseEntity<Message> signIn(@Valid @RequestBody @NotNull CustomUser customUser) {
        authenticationService.signIn(customUser);
        return ResponseEntity.ok(new Message(MESSAGE_IS_PENDING.getDescription()));
    }

    @PostMapping("/api/sign-in")
    @Validated(TokenObjectValidationGroups.class)
    public ResponseEntity<String> getBarrierToken(@Valid @RequestBody @NotBlank OneTimeTokenDto oneTimeTokenDto) {
        Optional<String> optionalJwtToken = authenticationService.getBarrierToken(oneTimeTokenDto);
        if (optionalJwtToken.isPresent()) {
            String jwtToken = optionalJwtToken.get();
            return ResponseEntity.ok(jwtToken);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/api/security")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> barrierToken(@Valid @RequestBody @NotBlank String string) {
        return ResponseEntity.ok("success");
    }
}

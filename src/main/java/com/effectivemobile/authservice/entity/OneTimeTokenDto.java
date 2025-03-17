package com.effectivemobile.authservice.entity;

import com.effectivemobile.authservice.other.validationgroups.EmailObjectValidationGroup;
import com.effectivemobile.authservice.other.validationgroups.ObjectWithAllPendingFieldsValidatonGroup;
import com.effectivemobile.authservice.other.validationgroups.TokenObjectValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class OneTimeTokenDto {

    @Size(min = 36, max = 36, message = "UUID must be 36 characters", groups = {ObjectWithAllPendingFieldsValidatonGroup.class,
            TokenObjectValidationGroups.class})
    @Null(message = "UUID time must be null", groups = EmailObjectValidationGroup.class)
    private String userToken;

    @Email(message = "Email is not valid", groups = {EmailObjectValidationGroup.class, ObjectWithAllPendingFieldsValidatonGroup.class})
    @Size(min = 5, max = 1000, message = "Email must be between 5 and 1000 characters", groups = {EmailObjectValidationGroup.class,
            ObjectWithAllPendingFieldsValidatonGroup.class})
    @Null(message = "Email must be null", groups = TokenObjectValidationGroups.class)
    private String email;

    @NotNull(message = "Created time cannot be null", groups = ObjectWithAllPendingFieldsValidatonGroup.class)
    @Null(message = "Created time must be null", groups = {EmailObjectValidationGroup.class, TokenObjectValidationGroups.class})
    private LocalDateTime createdTime;

    @NotNull(message = "Expired time cannot be null", groups = ObjectWithAllPendingFieldsValidatonGroup.class)
    @Null(message = "Expire time must be null", groups = {EmailObjectValidationGroup.class, TokenObjectValidationGroups.class})
    private LocalDateTime expiredTime;

    @NotNull(message = "Used cannot be null", groups = ObjectWithAllPendingFieldsValidatonGroup.class)
    @Null(message = "Used time must be null", groups = {EmailObjectValidationGroup.class, TokenObjectValidationGroups.class})
    private Boolean used;
}

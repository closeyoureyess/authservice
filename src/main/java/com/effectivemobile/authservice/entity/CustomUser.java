package com.effectivemobile.authservice.entity;

import com.effectivemobile.authservice.other.validationgroups.EmailObjectValidationGroup;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CustomUser {

    @Email(message = "Email don't be incorrect", groups = EmailObjectValidationGroup.class)
    private String email;

}

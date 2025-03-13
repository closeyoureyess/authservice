package com.effectivemobile.authservice.entity;

import com.effectivemobile.authservice.other.validationgroups.GroupOne;
import com.effectivemobile.authservice.other.validationgroups.GroupTwo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    @Size(min = 36, max = 36, message = "UUID must be 36 characters", groups = GroupOne.class)
    private String userToken;

    @Email(message = "Email is not valid", groups = GroupTwo.class)
    @Size(min = 5, max = 1000, message = "Email must be between 5 and 1000 characters", groups = GroupTwo.class)
    private String email;

    private LocalDateTime createdTime;

    private LocalDateTime expiredTime;

    private Boolean used;
}

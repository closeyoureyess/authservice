package com.effectivemobile.authservice.other;

import lombok.Getter;

@Getter
public enum MessageDescription {

    MESSAGE_IS_PENDING("An email with an activation code will be sent to your email within 15 minutes.");

    private String description;

    MessageDescription(String description) {
        this.description = description;
    }

}

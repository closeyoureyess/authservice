package com.effectivemobile.authservice.exceptions;

public class KafkaSenderRuntimeException extends RuntimeException {
    public KafkaSenderRuntimeException(String message) {
        super(message);
    }
}

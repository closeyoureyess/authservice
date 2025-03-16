package com.effectivemobile.authservice.controller;

import com.effectivemobile.authservice.exceptions.ApiErrorResponse;
import com.effectivemobile.authservice.exceptions.KafkaSenderRuntimeException;
import com.effectivemobile.authservice.exceptions.Violation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

import static com.effectivemobile.authservice.exceptions.ExceptionsDescription.TOPIC_OR_OBJECT_IN_KAFKA_IS_INCORRECT;

@ControllerAdvice
@Slf4j
public class HandlerExceptionController {

    /**
     * Обработчик ExecutorNotFoundException
     */
    @ExceptionHandler(KafkaSenderRuntimeException.class)
    protected ResponseEntity<ApiErrorResponse> handleExecutorNotFoundExeption(KafkaSenderRuntimeException e, HttpServletRequest request) {
        log.error("{}: {}\\n{}", TOPIC_OR_OBJECT_IN_KAFKA_IS_INCORRECT.getDescription(), e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("{}: {}\\n{}", "Exception", e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_GATEWAY,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiErrorResponse> handleException(RuntimeException e, HttpServletRequest request) {
        log.error("{}: {}\\n{}", "Exception", e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private ApiErrorResponse buildApiErrorResponse(HttpStatus status, String message, String path, List<Violation> violations) {
        return ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .violations(violations)
                .build();
    }
}

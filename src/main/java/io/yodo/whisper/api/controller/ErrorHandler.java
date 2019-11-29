package io.yodo.whisper.api.controller;

import io.yodo.whisper.api.error.ApiClientError;
import io.yodo.whisper.api.error.ApiGatewayError;
import io.yodo.whisper.commons.web.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ErrorHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleError(ApiGatewayError e) {
        return createResponse(e, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleError(ApiClientError e) {
        HttpStatus status = HttpStatus.resolve(e.getStatusCode());
        return createResponse(e, Objects.requireNonNullElse(status, HttpStatus.BAD_REQUEST));
    }

    private ResponseEntity<ErrorResponse> createResponse(Exception e, HttpStatus status) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e), status);
    }
}

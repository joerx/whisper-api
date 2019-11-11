package io.yodo.whisper.api.error;

import io.yodo.whisper.api.entity.ErrorResponse;
import org.springframework.http.ResponseEntity;

public class ApiResponseError extends RuntimeException {

    private final int statusCode;

    public ApiResponseError(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

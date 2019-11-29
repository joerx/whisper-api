package io.yodo.whisper.api.error;

public class ApiClientError extends AbstractHttpClientError {
    public ApiClientError(int statusCode, String message) {
        super(statusCode, message);
    }
}

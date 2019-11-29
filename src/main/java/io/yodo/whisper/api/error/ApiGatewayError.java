package io.yodo.whisper.api.error;

public class ApiGatewayError extends AbstractHttpClientError {
    public ApiGatewayError(int statusCode, String message) {
        super(statusCode, message);
    }
}

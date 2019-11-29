package io.yodo.whisper.api.error;

abstract class AbstractHttpClientError extends RuntimeException {

    private final int statusCode;

    AbstractHttpClientError(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

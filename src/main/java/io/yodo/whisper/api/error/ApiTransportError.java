package io.yodo.whisper.api.error;

/**
 * Exception class that indicates a general error in the communication with a remote API, (e.g. data codec errors,
 * i/o errors, http protocol errors) as opposed to application level errors. Please not that this does not include
 * non-200 HTTP status codes, these are considered application level errors and should cause an {@link ApiGatewayError}
 */
public class ApiTransportError extends RuntimeException {
    public ApiTransportError(Throwable cause) {
        super(cause);
    }
}

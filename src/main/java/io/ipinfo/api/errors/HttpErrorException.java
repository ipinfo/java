package io.ipinfo.api.errors;

/**
 * This covers all non 403 and 429 Http error statuses.
 */
public class HttpErrorException extends RuntimeException {
    private final int statusCode;

    public HttpErrorException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

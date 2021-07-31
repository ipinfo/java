package io.ipinfo.api.errors;

public class ErrorResponseException extends RuntimeException {
    public ErrorResponseException() {}

    public ErrorResponseException(Exception ex) {
        super(ex);
    }
}

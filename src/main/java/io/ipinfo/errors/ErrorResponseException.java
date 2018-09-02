package io.ipinfo.errors;

public class ErrorResponseException extends RuntimeException {

    public ErrorResponseException() {

    }

    public ErrorResponseException(Exception ex) {
        super(ex);
    }
}

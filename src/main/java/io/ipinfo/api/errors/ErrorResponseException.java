package io.ipinfo.api.errors;

public class ErrorResponseException extends RuntimeException {

    private final int statusCode;

    public ErrorResponseException() {
        this.statusCode = -1;
    }

    public ErrorResponseException(Exception ex) {
        super(ex);
        this.statusCode = -1;
    }

    public ErrorResponseException(int statusCode, String responseBody) {
        super(
            "Unexpected API response (status " +
                statusCode +
                "): " +
                responseBody
        );
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

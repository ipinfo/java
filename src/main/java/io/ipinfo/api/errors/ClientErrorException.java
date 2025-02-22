package io.ipinfo.api.errors;

/**
 * <p>This exception is raised when a client error is occurring (Http status code like 4xx).</p>
 *
 * <p>Note that HTTP status 403 (Forbidden) is now reported as a checked exception of type @see InvalidTokenException .</p>
 */
public class ClientErrorException extends HttpErrorException {
    public ClientErrorException(int statusCode, String message) {
        super(statusCode, message);
    }
}

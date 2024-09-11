package io.ipinfo.api.errors;

/**
 * This exception is raised when a server error is returned by IpInfo (Http status like 5xx)
 */
public class ServerErrorException extends HttpErrorException {
    public ServerErrorException(int statusCode, String message) {
        super(statusCode, message);
    }
}

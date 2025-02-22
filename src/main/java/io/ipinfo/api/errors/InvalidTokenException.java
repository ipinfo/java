package io.ipinfo.api.errors;

/**
 * <p>This exception is raised when the service returns a 403 (access denied).</p>
 *
 * <p>That likely indicates that the token is either missing, incorrect or expired.
 * It is a checked exception so, if you have multiple tokens (example during transition), you can fall back to another token.</p>
 */
public class InvalidTokenException extends Exception {
    public InvalidTokenException() {
        super("The server did not accepted the provided token or no token was provided.");
    }
}

package io.ipinfo.errors;

public class RateLimitedException extends Exception {

    public RateLimitedException() {
        super("You have been sending too many requests. Visit https://ipinfo.io/account to see your API limits.");
    }
}

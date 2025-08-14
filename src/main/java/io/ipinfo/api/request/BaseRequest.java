package io.ipinfo.api.request;

import com.google.gson.Gson;
import io.ipinfo.api.errors.*;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseRequest<T> {
    protected final static Gson gson = new Gson();
    private final OkHttpClient client;
    private final String token;

    protected BaseRequest(OkHttpClient client, String token) {
        this.client = client;
        this.token = token;
    }

    public abstract T handle() throws RateLimitedException, InvalidTokenException;

    public Response handleRequest(Request.Builder request) throws RateLimitedException, InvalidTokenException {
        request
                .addHeader("Authorization", Credentials.basic(token, ""))
                .addHeader("user-agent", "IPinfoClient/Java/3.1.0")
                .addHeader("Content-Type", "application/json");

        Response response;

        try {
            response = client.newCall(request.build()).execute();
        } catch (Exception e) {
            throw new ErrorResponseException(e);
        }

        // Sanity check
        if (response == null) {
            return null;
        }

        if (response.code() == 429) {
            throw new RateLimitedException();
        } else if ((response.code() == 403)) {
            throw new InvalidTokenException();
        } else if ((response.code() >= 400) && (response.code() <= 499)) {
            throw new ClientErrorException(response.code(), "Http error " + response.code() + ". " + response.message());
        } else if ((response.code() >= 500) && (response.code() <= 599)) {
            throw new ServerErrorException(response.code(), "Http error " + response.code() + ". " + response.message());
        }

        return response;
    }
}


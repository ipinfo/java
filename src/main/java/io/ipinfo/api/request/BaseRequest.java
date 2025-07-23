package io.ipinfo.api.request;

import com.google.gson.Gson;
import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
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

    public abstract T handle() throws RateLimitedException;

    public Response handleRequest(Request.Builder request) throws RateLimitedException {
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
        }

        return response;
    }
}


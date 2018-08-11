package io.ipinfo.request;

import com.google.gson.Gson;
import io.ipinfo.errors.RateLimitedException;
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

    public Response handleRequest(Request.Builder request) {
        request
                .addHeader("Authorization", Credentials.basic(token, ""))
                .addHeader("user-agent", "IPinfoClient/Java/1.0");
        try {
            return client.newCall(request.build()).execute();
        } catch (Exception e) {
            return null;
        }
    }
}


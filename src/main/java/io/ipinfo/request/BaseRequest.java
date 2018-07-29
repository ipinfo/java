package io.ipinfo.request;

import com.google.gson.Gson;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class BaseRequest<T> {
    protected final static Gson gson = new Gson();

    private final OkHttpClient client;
    private final ExecutorService executorService;
    private final String token;

    protected BaseRequest(OkHttpClient client, ExecutorService executorService, String token) {
        this.client = client;
        this.executorService = executorService;
        this.token = token;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public abstract Future<T> handle();

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


package io.ipinfo.request;

import io.ipinfo.errors.ErrorResponseException;
import io.ipinfo.errors.RateLimitedException;
import io.ipinfo.model.IPResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class IPRequest extends BaseRequest<IPResponse> {
    private final static String URL_FORMAT = "https://ipinfo.io/%s/json";
    private final String ip;

    public IPRequest(OkHttpClient client, ExecutorService executorService, String token, String ip) {
        super(client, executorService, token);
        this.ip = ip;
    }

    @Override
    public Future<IPResponse> handle() {
        String url = String.format(URL_FORMAT, ip);
        Request.Builder request = new Request.Builder().url(url).get();

        return getExecutorService().submit(() -> {
            try (Response response = handleRequest(request)) {
                if (response == null || response.body() == null) return null;

                if (response.code() == 429) {
                    throw new RateLimitedException();
                }

                try {
                    return gson.fromJson(response.body().string(), IPResponse.class);
                } catch (Exception ex) {
                    throw new ErrorResponseException();
                }
            }
        });
    }
}

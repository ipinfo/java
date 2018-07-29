package io.ipinfo.request;

import io.ipinfo.errors.ErrorResponseError;
import io.ipinfo.errors.RateLimitedException;
import io.ipinfo.model.IpResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class IpRequest extends BaseRequest<IpResponse> {
    private final static String URL_FORMAT = "https://ipinfo.io/%s/json";
    private final String ip;

    public IpRequest(OkHttpClient client, ExecutorService executorService, String token, String ip) {
        super(client, executorService, token);
        this.ip = ip;
    }

    @Override
    public Future<IpResponse> handle() {
        String url = String.format(URL_FORMAT, ip);
        Request.Builder request = new Request.Builder().url(url).get();

        return getExecutorService().submit(() -> {
            try (Response response = handleRequest(request)) {
                if (response == null) return null;

                if (response.code() == 429) {
                    throw new RateLimitedException();
                }

                try {
                    return gson.fromJson(response.body().string(), IpResponse.class);
                } catch (Exception ex) {
                    throw new ErrorResponseError();
                }
            }
        });
    }
}

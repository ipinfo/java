package io.ipinfo.request;

import io.ipinfo.errors.ErrorResponseException;
import io.ipinfo.errors.RateLimitedException;
import io.ipinfo.model.ASNResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ASNRequest extends BaseRequest<ASNResponse> {
    private final static String URL_FORMAT = "https://ipinfo.io/%s/json";
    private final String asn;

    public ASNRequest(OkHttpClient client, ExecutorService executorService, String token, String asn) {
        super(client, executorService, token);
        this.asn = asn;
    }

    @Override
    public Future<ASNResponse> handle() {
        String url = String.format(URL_FORMAT, asn);
        Request.Builder request = new Request.Builder().url(url).get();

        return getExecutorService().submit(() -> {
            try (Response response = handleRequest(request)) {
                if (response == null || response.body() == null) return null;

                if (response.code() == 429) {
                    throw new RateLimitedException();
                }

                try {
                    return gson.fromJson(response.body().string(), ASNResponse.class);
                } catch (Exception ex) {
                    throw new ErrorResponseException();
                }
            }
        });
    }
}

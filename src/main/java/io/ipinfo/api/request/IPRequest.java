package io.ipinfo.api.request;

import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IPRequest extends BaseRequest<IPResponse> {
    private final static String URL_FORMAT = "https://ipinfo.io/%s";
    private final String ip;

    public IPRequest(OkHttpClient client, String token, String ip) {
        super(client, token);
        this.ip = ip;
    }

    @Override
    public IPResponse handle() throws RateLimitedException {
        String url = String.format(URL_FORMAT, ip);
        Request.Builder request = new Request.Builder().url(url).get();

        try (Response response = handleRequest(request)) {
            if (response == null || response.body() == null) {
                return null;
            }

            try {
                return gson.fromJson(response.body().string(), IPResponse.class);
            } catch (Exception ex) {
                throw new ErrorResponseException(ex);
            }
        }
    }
}

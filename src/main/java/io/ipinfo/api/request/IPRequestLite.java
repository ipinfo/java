package io.ipinfo.api.request;

import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponseLite;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IPRequestLite extends BaseRequest<IPResponseLite> {
    private final static String URL_FORMAT = "https://api.ipinfo.io/lite/%s";
    private final String ip;

    public IPRequestLite(OkHttpClient client, String token, String ip) {
        super(client, token);
        this.ip = ip;
    }

    @Override
    public IPResponseLite handle() throws RateLimitedException {
        if (IPRequest.isBogon(ip)) {
            try {
                return new IPResponseLite(ip, true);
            } catch (Exception ex) {
                throw new ErrorResponseException(ex);
            }
        }

        String url = String.format(URL_FORMAT, ip);
        Request.Builder request = new Request.Builder().url(url).get();

        try (Response response = handleRequest(request)) {
            if (response == null || response.body() == null) {
                return null;
            }

            try {
                return gson.fromJson(response.body().string(), IPResponseLite.class);
            } catch (Exception ex) {
                throw new ErrorResponseException(ex);
            }
        }
    }
}

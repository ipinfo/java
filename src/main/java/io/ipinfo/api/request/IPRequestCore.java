package io.ipinfo.api.request;

import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponseCore;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IPRequestCore extends BaseRequest<IPResponseCore> {
    private final static String URL_FORMAT = "https://api.ipinfo.io/lookup/%s";
    private final String ip;

    public IPRequestCore(OkHttpClient client, String token, String ip) {
        super(client, token);
        this.ip = ip;
    }

    @Override
    public IPResponseCore handle() throws RateLimitedException {
        if (IPRequest.isBogon(ip)) {
            try {
                return new IPResponseCore(ip, true);
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
                return gson.fromJson(response.body().string(), IPResponseCore.class);
            } catch (Exception ex) {
                throw new ErrorResponseException(ex);
            }
        }
    }
}

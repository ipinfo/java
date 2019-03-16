package io.ipinfo.api.request;

import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.ASNResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ASNRequest extends BaseRequest<ASNResponse> {
    private final static String URL_FORMAT = "https://ipinfo.io/%s";
    private final String asn;

    public ASNRequest(OkHttpClient client, String token, String asn) {
        super(client, token);
        this.asn = asn;
    }

    @Override
    public ASNResponse handle() throws RateLimitedException {
        String url = String.format(URL_FORMAT, asn);
        Request.Builder request = new Request.Builder().url(url).get();

        try (Response response = handleRequest(request)) {
            if (response == null || response.body() == null) return null;

            try {
                return gson.fromJson(response.body().string(), ASNResponse.class);
            } catch (Exception ex) {
                throw new ErrorResponseException(ex);
            }
        }
    }
}

package io.ipinfo.api.request;

import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.MapResponse;
import okhttp3.*;

import java.util.List;

public class MapRequest extends BaseRequest<MapResponse> {
    private final static String URL = "https://ipinfo.io/tools/map";
    private final List<String> ips;

    public MapRequest(OkHttpClient client, String token, List<String> ips) {
        super(client, token);
        this.ips = ips;
    }

    @Override
    public MapResponse handle() throws RateLimitedException {
        String jsonIpList = gson.toJson(ips);
        RequestBody requestBody = RequestBody.create(jsonIpList, JSON);
        Request.Builder request = new Request.Builder().url(URL).post(requestBody);

        try (Response response = handleRequest(request)) {
            if (response == null || response.body() == null) {
                return null;
            }

            try {
                return gson.fromJson(response.body().string(), MapResponse.class);
            } catch (Exception ex) {
                throw new ErrorResponseException(ex);
            }
        }
    }
}

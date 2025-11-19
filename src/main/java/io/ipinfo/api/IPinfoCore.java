package io.ipinfo.api;

import io.ipinfo.api.cache.Cache;
import io.ipinfo.api.cache.SimpleCache;
import io.ipinfo.api.context.Context;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponseCore;
import io.ipinfo.api.request.IPRequestCore;
import java.time.Duration;
import okhttp3.OkHttpClient;

public class IPinfoCore {

    private final OkHttpClient client;
    private final Context context;
    private final String token;
    private final Cache cache;

    IPinfoCore(
        OkHttpClient client,
        Context context,
        String token,
        Cache cache
    ) {
        this.client = client;
        this.context = context;
        this.token = token;
        this.cache = cache;
    }

    public static void main(String[] args) throws RateLimitedException {
        System.out.println("Running IPinfo Core client");
    }

    /**
     * Lookup IP information using the IP. This is a blocking call.
     *
     * @param ip IP address to query information for.
     * @return Response containing IP information.
     * @throws RateLimitedException if the user has exceeded the rate limit.
     */
    public IPResponseCore lookupIP(String ip) throws RateLimitedException {
        IPRequestCore request = new IPRequestCore(client, token, ip);
        IPResponseCore response = request.handle();

        if (response != null) {
            response.setContext(context);
            if (!response.getBogon()) {
                cache.set(cacheKey(ip), response);
            }
        }

        return response;
    }

    public static String cacheKey(String k) {
        return "core_" + k;
    }

    public static class Builder {

        private OkHttpClient client;
        private String token;
        private Cache cache;

        public Builder setClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setCache(Cache cache) {
            this.cache = cache;
            return this;
        }

        public IPinfoCore build() {
            if (client == null) {
                client = new OkHttpClient();
            }
            if (cache == null) {
                cache = new SimpleCache(Duration.ofDays(1));
            }
            return new IPinfoCore(client, new Context(), token, cache);
        }
    }
}

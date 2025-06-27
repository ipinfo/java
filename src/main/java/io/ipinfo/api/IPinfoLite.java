package io.ipinfo.api;

import com.google.common.net.InetAddresses;
import io.ipinfo.api.cache.Cache;
import io.ipinfo.api.cache.SimpleCache;
import io.ipinfo.api.context.Context;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponseLite;
import io.ipinfo.api.request.IPRequestLite;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import javax.annotation.ParametersAreNonnullByDefault;
import okhttp3.*;

public class IPinfoLite {

    private final OkHttpClient client;
    private final Context context;
    private final String token;
    private final Cache cache;

    IPinfoLite(
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
        System.out.println("Running IPinfo Lite client");
    }

    /**
     * Lookup IP information using the IP. This is a blocking call.
     *
     * @param ip IP address to query information for.
     * @return Response containing IP information.
     * @throws RateLimitedException if the user has exceeded the rate limit.
     */
    public IPResponseLite lookupIP(String ip) throws RateLimitedException {
        IPRequestLite request = new IPRequestLite(client, token, ip);
        IPResponseLite response = request.handle();

        if (response != null) {
            response.setContext(context);
            if (!response.getBogon()) {
                cache.set(cacheKey(ip), response);
            }
        }

        return response;
    }

    public static String cacheKey(String k) {
        return "lite_" + k;
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

        public IPinfoLite build() {
            if (client == null) {
                client = new OkHttpClient();
            }
            if (cache == null) {
                cache = new SimpleCache(Duration.ofDays(1));
            }
            return new IPinfoLite(client, new Context(), token, cache);
        }
    }
}

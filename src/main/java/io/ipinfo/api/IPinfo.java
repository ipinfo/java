package io.ipinfo.api;

import com.google.common.net.InetAddresses;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.ipinfo.api.cache.Cache;
import io.ipinfo.api.cache.SimpleCache;
import io.ipinfo.api.context.Context;
import io.ipinfo.api.errors.InvalidTokenException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.api.model.MapResponse;
import io.ipinfo.api.request.ASNRequest;
import io.ipinfo.api.request.IPRequest;
import io.ipinfo.api.request.MapRequest;
import okhttp3.*;

import javax.annotation.ParametersAreNonnullByDefault;
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

public class IPinfo {
    private static final int batchMaxSize = 1000;
    private static final int batchReqTimeoutDefault = 5;
    private static final BatchReqOpts defaultBatchReqOpts = new BatchReqOpts.Builder()
            .setBatchSize(batchMaxSize)
            .setTimeoutPerBatch(batchReqTimeoutDefault)
            .build();
    private final static Gson gson = new Gson();

    private final OkHttpClient client;
    private final Context context;
    private final String token;
    private final Cache cache;

    IPinfo(OkHttpClient client, Context context, String token, Cache cache) {
        this.client = client;
        this.context = context;
        this.token = token;
        this.cache = cache;
    }

    public static void main(String... args) {
        System.out.println("This library is not meant to be run as a standalone jar.");
        System.exit(0);
    }

    /**
     * Lookup IP information using the IP.
     *
     * @param ip the ip string to lookup - accepts both ipv4 and ipv6.
     * @return IPResponse response from the api.
     * @throws RateLimitedException an exception when your api key has been rate limited.
     */
    public IPResponse lookupIP(String ip) throws RateLimitedException, InvalidTokenException {
        IPResponse response = (IPResponse)cache.get(cacheKey(ip));
        if (response != null) {
            return response;
        }

        response = new IPRequest(client, token, ip).handle();
        response.setContext(context);

        cache.set(cacheKey(ip), response);
        return response;
    }

    /**
     * Lookup ASN information using the AS number.
     *
     * @param asn the asn string to lookup.
     * @return ASNResponse response from the api.
     * @throws RateLimitedException an exception when your api key has been rate limited.
     */
    public ASNResponse lookupASN(String asn) throws RateLimitedException, InvalidTokenException {
        ASNResponse response = (ASNResponse)cache.get(cacheKey(asn));
        if (response != null) {
            return response;
        }

        response = new ASNRequest(client, token, asn).handle();
        response.setContext(context);

        cache.set(cacheKey(asn), response);
        return response;
    }

    /**
     * Get a map of a list of IPs.
     *
     * @param ips the list of IPs to map.
     * @return String the URL to the map.
     * @throws RateLimitedException an exception when your API key has been rate limited.
     */
    public String getMap(List<String> ips) throws RateLimitedException, InvalidTokenException {
        MapResponse response = new MapRequest(client, token, ips).handle();
        return response.getReportUrl();
    }

    /**
     * Get the result of a list of URLs in bulk.
     *
     * @param urls the list of URLs.
     * @return the result where each URL is the key and the value is the data for that URL.
     * @throws RateLimitedException an exception when your API key has been rate limited.
     */
    public ConcurrentHashMap<String, Object> getBatch(
            List<String> urls
    ) throws RateLimitedException {
        return this.getBatchGeneric(urls, defaultBatchReqOpts);
    }

    /**
     * Get the result of a list of URLs in bulk.
     *
     * @param urls the list of URLs.
     * @param opts options to modify the behavior of the batch operation.
     * @return the result where each URL is the key and the value is the data for that URL.
     * @throws RateLimitedException an exception when your API key has been rate limited.
     */
    public ConcurrentHashMap<String, Object> getBatch(
            List<String> urls,
            BatchReqOpts opts
    ) throws RateLimitedException {
        return this.getBatchGeneric(urls, opts);
    }

    /**
     * Get the result of a list of IPs in bulk.
     *
     * @param ips the list of IPs.
     * @return the result where each IP is the key and the value is the data for that IP.
     * @throws RateLimitedException an exception when your API key has been rate limited.
     */
    public ConcurrentHashMap<String, IPResponse> getBatchIps(
            List<String> ips
    ) throws RateLimitedException {
        return new ConcurrentHashMap(this.getBatchGeneric(ips, defaultBatchReqOpts));
    }

    /**
     * Get the result of a list of IPs in bulk.
     *
     * @param ips the list of IPs.
     * @param opts options to modify the behavior of the batch operation.
     * @return the result where each IP is the key and the value is the data for that IP.
     * @throws RateLimitedException an exception when your API key has been rate limited.
     */
    public ConcurrentHashMap<String, IPResponse> getBatchIps(
            List<String> ips,
            BatchReqOpts opts
    ) throws RateLimitedException {
        return new ConcurrentHashMap(this.getBatchGeneric(ips, opts));
    }

    /**
     * Get the result of a list of ASNs in bulk.
     *
     * @param asns the list of ASNs.
     * @return the result where each ASN is the key and the value is the data for that ASN.
     * @throws RateLimitedException an exception when your API key has been rate limited.
     */
    public ConcurrentHashMap<String, ASNResponse> getBatchAsns(
            List<String> asns
    ) throws RateLimitedException {
        return new ConcurrentHashMap(this.getBatchGeneric(asns, defaultBatchReqOpts));
    }

    /**
     * Get the result of a list of ASNs in bulk.
     *
     * @param asns the list of ASNs.
     * @param opts options to modify the behavior of the batch operation.
     * @return the result where each ASN is the key and the value is the data for that ASN.
     * @throws RateLimitedException an exception when your API key has been rate limited.
     */
    public ConcurrentHashMap<String, ASNResponse> getBatchAsns(
            List<String> asns,
            BatchReqOpts opts
    ) throws RateLimitedException {
        return new ConcurrentHashMap(this.getBatchGeneric(asns, opts));
    }

    private ConcurrentHashMap<String, Object> getBatchGeneric(
            List<String> urls,
            BatchReqOpts opts
    ) throws RateLimitedException {
        int batchSize;
        int timeoutPerBatch;
        List<String> lookupUrls;
        ConcurrentHashMap<String, Object> result;

        // if the cache is available, filter out URLs already cached.
        result = new ConcurrentHashMap<>(urls.size());
        if (this.cache != null) {
            lookupUrls = new ArrayList<>(urls.size()/2);
            for (String url : urls) {
                Object val = cache.get(cacheKey(url));
                if (val != null) {
                    result.put(url, val);
                } else {
                    lookupUrls.add(url);
                }
            }
        } else {
            lookupUrls = urls;
        }

        // everything cached; exit early.
        if (lookupUrls.size() == 0) {
            return result;
        }

        // use correct batch size; default/clip to `batchMaxSize`.
        if (opts.batchSize == 0 || opts.batchSize > batchMaxSize) {
            batchSize = batchMaxSize;
        } else {
            batchSize = opts.batchSize;
        }

        // use correct timeout per batch; either default or user-provided.
        if (opts.timeoutPerBatch == 0) {
            timeoutPerBatch = batchReqTimeoutDefault;
        } else {
            timeoutPerBatch = opts.timeoutPerBatch;
        }

        // prep URL we'll target.
        // add `filter=1` as qparam for filtering out empty results on server.
        String postUrl;
        if (opts.filter) {
            postUrl = "https://ipinfo.io/batch?filter=1";
        } else {
            postUrl = "https://ipinfo.io/batch";
        }

        // prepare latch & common request.
        // each request, when complete, will countdown the latch.
        CountDownLatch latch = new CountDownLatch((int)Math.ceil(lookupUrls.size()/1000.0));
        Request.Builder reqCommon = new Request.Builder()
                .url(postUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Credentials.basic(token, ""))
                .addHeader("User-Agent", "IPinfoClient/Java/3.0.2");

        for (int i = 0; i < lookupUrls.size(); i += batchSize) {
            // create chunk.
            int end = i + batchSize;
            if (end > lookupUrls.size()) {
                end = lookupUrls.size();
            }
            List<String> urlsChunk = lookupUrls.subList(i, end);

            // prepare & queue up request.
            String urlListJson = gson.toJson(urlsChunk);
            RequestBody requestBody = RequestBody.create(null, urlListJson);
            Request req = reqCommon.post(requestBody).build();
            OkHttpClient chunkClient = client.newBuilder()
                    .connectTimeout(timeoutPerBatch, TimeUnit.SECONDS)
                    .readTimeout(timeoutPerBatch, TimeUnit.SECONDS)
                    .build();
            chunkClient.newCall(req).enqueue(new Callback() {
                @Override
                @ParametersAreNonnullByDefault
                public void onFailure(Call call, IOException e) {
                    latch.countDown();
                }

                @Override
                @ParametersAreNonnullByDefault
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.body() == null || response.code() == 429) {
                        return;
                    }

                    Type respType = new TypeToken<HashMap<String, Object>>() {}.getType();
                    HashMap<String, Object> localResult
                            = gson.fromJson(response.body().string(), respType);
                    localResult.forEach(new BiConsumer<String, Object>() {
                        @Override
                        public void accept(String k, Object v) {
                            if (k.startsWith("AS")) {
                                String vStr = gson.toJson(v);
                                ASNResponse vCasted = gson.fromJson(vStr, ASNResponse.class);
                                vCasted.setContext(context);
                                result.put(k, vCasted);
                            } else if (InetAddresses.isInetAddress(k)) {
                                String vStr = gson.toJson(v);
                                IPResponse vCasted = gson.fromJson(vStr, IPResponse.class);
                                vCasted.setContext(context);
                                result.put(k, vCasted);
                            } else {
                                result.put(k, v);
                            }
                        }
                    });

                    latch.countDown();
                }
            });
        }

        // wait for all requests to finish.
        try {
            if (opts.timeoutTotal == 0) {
                latch.await();
            } else {
                boolean success = latch.await(opts.timeoutTotal, TimeUnit.SECONDS);
                if (!success) {
                    if (result.size() == 0) {
                        return null;
                    } else {
                        return result;
                    }
                }
            }
        } catch (InterruptedException e) {
            if (result.size() == 0) {
                return null;
            } else {
                return result;
            }
        }

        // insert any new lookups into the cache:
        if (cache != null) {
            for (String url : lookupUrls) {
                Object v = result.get(url);
                if (v != null) {
                    cache.set(cacheKey(url), v);
                }
            }
        }

        return result;
    }

    /**
     * Converts a normal key into a versioned cache key.
     *
     * @param k the key to convert into a versioned cache key.
     * @return the versioned cache key.
     */
    public static String cacheKey(String k) {
        return k+":1";
    }

    public static class Builder {
        private OkHttpClient client = new OkHttpClient.Builder().build();
        private String token = "";
        private Cache cache = new SimpleCache(Duration.ofDays(1));

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

        public IPinfo build() {
            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException("A token must be provided.");
            }
            return new IPinfo(client, new Context(), token, cache);
        }
    }

    public static class BatchReqOpts {
        public final int batchSize;
        public final int timeoutPerBatch;
        public final int timeoutTotal;
        public final boolean filter;

        public BatchReqOpts(
                int batchSize,
                int timeoutPerBatch,
                int timeoutTotal,
                boolean filter
        ) {
            this.batchSize = batchSize;
            this.timeoutPerBatch = timeoutPerBatch;
            this.timeoutTotal = timeoutTotal;
            this.filter = filter;
        }

        public static class Builder {
            private int batchSize = 1000;
            private int timeoutPerBatch = 5;
            private int timeoutTotal = 0;
            private boolean filter = false;

            /**
             * batchSize is the internal batch size used per API request; the IPinfo
             * API has a maximum batch size, but the batch request functions available
             * in this library do not. Therefore the library chunks the input slices
             * internally into chunks of size `batchSize`, clipping to the maximum
             * allowed by the IPinfo API.
             *
             * 0 means to use the default batch size which is the max allowed by the
             * IPinfo API.
             *
             * @param batchSize see description.
             * @return the builder.
             */
            public Builder setBatchSize(int batchSize) {
                this.batchSize = batchSize;
                return this;
            }

            /**
             * timeoutPerBatch is the timeout in seconds that each batch of size
             * `BatchSize` will have for its own request.
             *
             * 0 means to use a default of 5 seconds; any negative number will turn it
             * off; turning it off does _not_ disable the effects of `timeoutTotal`.
             *
             * @param timeoutPerBatch see description.
             * @return the builder.
             */
            public Builder setTimeoutPerBatch(int timeoutPerBatch) {
                this.timeoutPerBatch = timeoutPerBatch;
                return this;
            }

            /**
             * timeoutTotal is the total timeout in seconds for all batch requests in a
             * batch request function to complete.
             *
             * 0 means no total timeout; `timeoutPerBatch` will still apply.
             *
             * @param timeoutTotal see description.
             * @return the builder.
             */
            public Builder setTimeoutTotal(int timeoutTotal) {
                this.timeoutTotal = timeoutTotal;
                return this;
            }

            /**
             * filter, if turned on, will filter out a URL whose value was deemed empty
             * on the server.
             *
             * @param filter see description.
             * @return the builder.
             */
            public Builder setFilter(boolean filter) {
                this.filter = filter;
                return this;
            }

            public IPinfo.BatchReqOpts build() {
                return new IPinfo.BatchReqOpts(batchSize, timeoutPerBatch, timeoutTotal, filter);
            }
        }
    }
}

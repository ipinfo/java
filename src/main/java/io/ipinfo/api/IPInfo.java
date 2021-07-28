package io.ipinfo.api;

import com.google.gson.Gson;
import io.ipinfo.api.cache.Cache;
import io.ipinfo.api.context.Context;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.api.model.MapResponse;
import io.ipinfo.api.request.ASNRequest;
import io.ipinfo.api.request.IPRequest;
import io.ipinfo.api.request.MapRequest;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class IPInfo {
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

    IPInfo(OkHttpClient client, Context context, String token, Cache cache) {
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
     * Gets the builder for IPInfo
     *
     * @return IPInfoBuilder object
     */
    public static IPInfoBuilder builder() {
        return new IPInfoBuilder();
    }

    /**
     * Lookup IP information using the IP.
     *
     * @param ip the ip string to lookup - accepts both ipv4 and ipv6.
     * @return IPResponse response from the api.
     * @throws RateLimitedException an exception when your api key has been rate limited.
     */
    public IPResponse lookupIP(String ip) throws RateLimitedException {
        IPResponse response = cache.getIp(ip);
        if (response != null) {
            return response;
        }

        response = new IPRequest(client, token, ip).handle();
        response.setContext(context);

        cache.setIp(ip, response);
        return response;
    }

    /**
     * Lookup ASN information using the AS number.
     *
     * @param asn the asn string to lookup.
     * @return ASNResponse response from the api.
     * @throws RateLimitedException an exception when your api key has been rate limited.
     */
    public ASNResponse lookupASN(String asn) throws RateLimitedException {
        ASNResponse response = cache.getAsn(asn);
        if (response != null) {
            return response;
        }

        response = new ASNRequest(client, token, asn).handle();
        response.setContext(context);

        cache.setAsn(asn, response);
        return response;
    }

    /**
     * Get a map of a list of IPs.
     *
     * @param ips the list of IPs to map.
     * @return String the URL to the map.
     * @throws RateLimitedException an exception when your API key has been rate limited.
     */
    public String getMap(List<String> ips) throws RateLimitedException {
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
                Object val = cache.get(url);
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

        // TODO need to use timeoutPerBatch somehow; it must be on the client
        // rather than on the request.
        CountDownLatch latch = new CountDownLatch((int)Math.ceil(lookupUrls.size()/1000.0));
        Request.Builder reqCommon = new Request.Builder()
                .url(postUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Credentials.basic(token, ""))
                .addHeader("User-Agent", "IPinfoClient/Java/1.0");

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
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    latch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // TODO
                    // - convert resp json into generic hashmap.
                    // - for each url, convert obj into one of ASNResponse, IPResponse or Object.
                    ConcurrentHashMap<String, Object> localResult
                            = new ConcurrentHashMap<>(urlsChunk.size());

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

        // we delay inserting into the cache until now because:
        // 1. it's likely more cache-line friendly.
        // 2. doing it while updating `result` inside the request workers would be
        //    problematic if the cache is external since we take a mutex lock for
        //    that entire period.
        if (cache != null) {
            for (String url : lookupUrls) {
                Object v = result.get(url);
                if (v != null) {
                    cache.set(url, v);
                }
            }
        }

        return result;
    }

    public static class BatchReqOpts {
        public final int batchSize;
        public final int timeoutPerBatch;
        public final int timeoutTotal;
        public final boolean filter;

        public BatchReqOpts(int batchSize, int timeoutPerBatch, int timeoutTotal, boolean filter) {
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

            public IPInfo.BatchReqOpts build() {
                return new IPInfo.BatchReqOpts(batchSize, timeoutPerBatch, timeoutTotal, filter);
            }
        }
    }
}

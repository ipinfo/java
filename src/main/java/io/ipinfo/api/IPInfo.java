package io.ipinfo.api;

import io.ipinfo.api.cache.Cache;
import io.ipinfo.api.context.Context;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.api.request.ASNRequest;
import io.ipinfo.api.request.IPRequest;
import okhttp3.OkHttpClient;

public class IPInfo {
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
     * @return
     */
    public static IPInfoBuilder builder() {
        return new IPInfoBuilder();
    }

    /**
     * Lookup IP information using the IP.
     *
     * @param ip
     * @return IPResponse
     * @throws RateLimitedException
     */
    public IPResponse lookupIP(String ip) throws RateLimitedException {
        IPResponse response = cache.getIp(ip);
        if (response != null) return response;

        response = new IPRequest(client, token, ip).handle();
        response.setContext(context);

        cache.setIp(ip, response);
        return response;
    }

    /**
     * Lookup ASN information using the AS number.
     *
     * @param asn
     * @return ASNResponse
     * @throws RateLimitedException
     */
    public ASNResponse lookupASN(String asn) throws RateLimitedException {
        ASNResponse response = cache.getAsn(asn);
        if (response != null) return response;

        response = new ASNRequest(client, token, asn).handle();
        response.setContext(context);

        cache.setAsn(asn, response);
        return response;
    }
}

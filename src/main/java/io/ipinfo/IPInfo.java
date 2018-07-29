package io.ipinfo;

import io.ipinfo.model.ASNResponse;
import io.ipinfo.model.IPResponse;
import io.ipinfo.request.ASNRequest;
import io.ipinfo.request.IPRequest;
import okhttp3.OkHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class IPInfo {
    private final ExecutorService service;
    private final OkHttpClient client;
    private final String token;

    IPInfo(ExecutorService service, OkHttpClient client, String token) {
        this.service = service;
        this.client = client;
        this.token = token;
    }

    public static void main(String... args) {
        System.out.println("This library is not meant to be run as a standalone jar.");

        System.exit(0);
    }

    /**
     * Gets the builder for IPInfo
     * @return
     */
    public static IPInfoBuilder builder() {
        return new IPInfoBuilder();
    }

    /**
     * Lookup IP information using the IP.
     *
     * @param ip
     * @return IPResponse future - This may throw a RateLimitedException inside ExecutionException when get() is called on it
     */
    public Future<IPResponse> lookupIP(String ip) {
        return new IPRequest(client, service, token, ip).handle();
    }

    /**
     * Lookup ASN information using the AS number.
     *
     * @param asn
     * @return ASNResponse future - This may throw a RateLimitedException inside ExecutionException when get() is called on it
     */
    public Future<ASNResponse> lookupASN(String asn) {
        return new ASNRequest(client, service, token, asn).handle();
    }
}

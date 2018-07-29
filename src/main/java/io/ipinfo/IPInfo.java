package io.ipinfo;

import io.ipinfo.model.AsnResponse;
import io.ipinfo.model.IpResponse;
import io.ipinfo.request.AsnRequest;
import io.ipinfo.request.IpRequest;
import okhttp3.OkHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class IPInfo {
    private final ExecutorService service;
    private final OkHttpClient client;
    private final String token;

    public IPInfo(ExecutorService service, OkHttpClient client, String token) {
        this.service = service;
        this.client = client;
        this.token = token;
    }

    public static void main(String... args) {
        System.out.println("This library is not meant to be run as a standalone jar.");

        System.exit(0);
    }

    /**
     * Lookup IP information using the IP.
     *
     * @param ip
     * @return IpResponse future - This may throw a RateLimitedException inside ExecutionException when get() is called on it
     */
    public Future<IpResponse> lookupIp(String ip) {
        return new IpRequest(client, service, token, ip).handle();
    }

    /**
     * Lookup ASN information using the AS number.
     *
     * @param asn
     * @return AsnResponse future - This may throw a RateLimitedException inside ExecutionException when get() is called on it
     */
    public Future<AsnResponse> lookupAsn(String asn) {
        return new AsnRequest(client, service, token, asn).handle();
    }
}

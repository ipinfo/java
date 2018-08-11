package io.ipinfo;

import io.ipinfo.errors.RateLimitedException;
import io.ipinfo.model.ASNResponse;
import io.ipinfo.model.IPResponse;
import io.ipinfo.request.ASNRequest;
import io.ipinfo.request.IPRequest;
import okhttp3.OkHttpClient;

import java.util.Map;

public class IPInfo {
    private final OkHttpClient client;
    private final String token;
    private final Map<String, String> countryMap;

    IPInfo(OkHttpClient client, String token, Map<String, String> countryMap) {
        this.client = client;
        this.token = token;
        this.countryMap = countryMap;
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
        return new IPRequest(client, token, ip).handle();
    }

    /**
     * Lookup ASN information using the AS number.
     *
     * @param asn
     * @return ASNResponse
     * @throws RateLimitedException
     */
    public ASNResponse lookupASN(String asn) throws RateLimitedException {
        return new ASNRequest(client, token, asn).handle();
    }

    /**
     * Looks up a country name.
     *
     * @param countryCode
     * @return The name of the country.
     */
    public String lookupCountryName(String countryCode) {
        return countryMap.getOrDefault(countryCode, null);
    }
}

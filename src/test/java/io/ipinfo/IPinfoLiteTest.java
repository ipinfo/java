package io.ipinfo;

import static org.junit.jupiter.api.Assertions.*;

import io.ipinfo.api.IPinfoLite;
import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponseLite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IPinfoLiteTest {

    @Test
    public void testGoogleDNSLite() {
        IPinfoLite ii = new IPinfoLite.Builder()
            .setToken(System.getenv("IPINFO_TOKEN"))
            .build();

        try {
            IPResponseLite response = ii.lookupIP("8.8.8.8");
            assertAll(
                "8.8.8.8",
                () -> assertEquals("8.8.8.8", response.getIp(), "IP mismatch"),
                () ->
                    assertEquals("AS15169", response.getAsn(), "ASN mismatch"),
                () ->
                    assertEquals(
                        "Google LLC",
                        response.getAsName(),
                        "AS name mismatch"
                    ),
                () ->
                    assertEquals(
                        "google.com",
                        response.getAsDomain(),
                        "AS domain mismatch"
                    ),
                () ->
                    assertEquals(
                        "US",
                        response.getCountryCode(),
                        "country code mismatch"
                    ),
                () ->
                    assertEquals(
                        "United States",
                        response.getCountry(),
                        "country mismatch"
                    ),
                () ->
                    assertEquals(
                        "United States",
                        response.getCountryName(),
                        "country name mismatch"
                    ),
                () ->
                    assertEquals(
                        "NA",
                        response.getContinentCode(),
                        "continent code mismatch"
                    ),
                () ->
                    assertEquals(
                        "North America",
                        response.getContinent(),
                        "continent mismatch"
                    ),
                () -> assertFalse(response.isEU(), "isEU mismatch"),
                () ->
                    assertEquals(
                        "🇺🇸",
                        response.getCountryFlag().getEmoji(),
                        "emoji mismatch"
                    ),
                () ->
                    assertEquals(
                        "U+1F1FA U+1F1F8",
                        response.getCountryFlag().getUnicode(),
                        "country flag unicode mismatch"
                    ),
                () ->
                    assertEquals(
                        "https://cdn.ipinfo.io/static/images/countries-flags/US.svg",
                        response.getCountryFlagURL(),
                        "country flag URL mismatch"
                    ),
                () ->
                    assertEquals(
                        "USD",
                        response.getCountryCurrency().getCode(),
                        "country currency code mismatch"
                    ),
                () ->
                    assertEquals(
                        "$",
                        response.getCountryCurrency().getSymbol(),
                        "country currency symbol mismatch"
                    ),
                () ->
                    assertEquals(
                        "NA",
                        response.getContinentInfo().getCode(),
                        "continent info code mismatch"
                    ),
                () ->
                    assertEquals(
                        "North America",
                        response.getContinentInfo().getName(),
                        "continent info name mismatch"
                    ),
                () -> assertFalse(response.getBogon(), "bogon mismatch")
            );

            IPResponseLite bogonResp = ii.lookupIP("2001:0:c000:200::0:255:1");
            assertAll(
                "bogon response",
                () ->
                    assertEquals(
                        "2001:0:c000:200::0:255:1",
                        bogonResp.getIp(),
                        "IP mismatch"
                    ),
                () -> assertTrue(bogonResp.getBogon(), "bogon mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testCloudFlareDNSLite() {
        IPinfoLite ii = new IPinfoLite.Builder()
            .setToken(System.getenv("IPINFO_TOKEN"))
            .build();

        try {
            IPResponseLite response = ii.lookupIP("1.1.1.1");
            assertAll(
                "1.1.1.1",
                () -> assertEquals("1.1.1.1", response.getIp(), "IP mismatch"),
                () ->
                    assertEquals("AS13335", response.getAsn(), "ASN mismatch"),
                () ->
                    assertEquals(
                        "Cloudflare, Inc.",
                        response.getAsName(),
                        "AS name mismatch"
                    ),
                () ->
                    assertEquals(
                        "cloudflare.com",
                        response.getAsDomain(),
                        "AS domain mismatch"
                    ),
                () ->
                    assertEquals(
                        "AU",
                        response.getCountryCode(),
                        "country code mismatch"
                    ),
                () ->
                    assertEquals(
                        "Australia",
                        response.getCountry(),
                        "country mismatch"
                    ),
                () ->
                    assertEquals(
                        "Australia",
                        response.getCountryName(),
                        "country name mismatch"
                    ),
                () ->
                    assertEquals(
                        "OC",
                        response.getContinentCode(),
                        "continent code mismatch"
                    ),
                () ->
                    assertEquals(
                        "Oceania",
                        response.getContinent(),
                        "continent mismatch"
                    ),
                () -> assertFalse(response.isEU(), "isEU mismatch"),
                () ->
                    assertEquals(
                        "🇦🇺",
                        response.getCountryFlag().getEmoji(),
                        "emoji mismatch"
                    ),
                () ->
                    assertEquals(
                        "U+1F1E6 U+1F1FA",
                        response.getCountryFlag().getUnicode(),
                        "country flag unicode mismatch"
                    ),
                () ->
                    assertEquals(
                        "https://cdn.ipinfo.io/static/images/countries-flags/AU.svg",
                        response.getCountryFlagURL(),
                        "country flag URL mismatch"
                    ),
                () ->
                    assertEquals(
                        "AUD",
                        response.getCountryCurrency().getCode(),
                        "country currency code mismatch"
                    ),
                () ->
                    assertEquals(
                        "$",
                        response.getCountryCurrency().getSymbol(),
                        "country currency symbol mismatch"
                    ),
                () ->
                    assertEquals(
                        "OC",
                        response.getContinentInfo().getCode(),
                        "continent info code mismatch"
                    ),
                () ->
                    assertEquals(
                        "Oceania",
                        response.getContinentInfo().getName(),
                        "continent info name mismatch"
                    ),
                () -> assertFalse(response.getBogon(), "bogon mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testBogonIPsLite() {
        IPinfoLite ii = new IPinfoLite.Builder()
            .setToken(System.getenv("IPINFO_TOKEN"))
            .build();

        try {
            // Test various bogon IPs
            String[] bogonIps = {
                "10.0.0.1", // Private network
                "192.168.1.1", // Private network
                "127.0.0.1", // Loopback
                "169.254.1.1", // Link-local
                "224.0.0.1", // Multicast
                "255.255.255.255", // Broadcast
            };

            for (String ip : bogonIps) {
                IPResponseLite response = ii.lookupIP(ip);
                assertAll(
                    "bogon IP " + ip,
                    () -> assertEquals(ip, response.getIp(), "IP mismatch"),
                    () -> assertTrue(response.getBogon(), "should be bogon")
                );
            }
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testEUCountryLite() {
        IPinfoLite ii = new IPinfoLite.Builder()
            .setToken(System.getenv("IPINFO_TOKEN"))
            .build();

        try {
            // Test with a German IP (should be EU)
            IPResponseLite response = ii.lookupIP("89.200.139.6"); // A German IP
            if (
                response != null &&
                !response.getBogon() &&
                "DE".equals(response.getCountryCode())
            ) {
                assertAll(
                    "EU country test",
                    () ->
                        assertEquals(
                            "DE",
                            response.getCountryCode(),
                            "country code mismatch"
                        ),
                    () ->
                        assertTrue(response.isEU(), "Germany should be in EU"),
                    () ->
                        assertEquals(
                            "🇩🇪",
                            response.getCountryFlag().getEmoji(),
                            "German flag emoji mismatch"
                        ),
                    () ->
                        assertEquals(
                            "EUR",
                            response.getCountryCurrency().getCode(),
                            "Euro currency mismatch"
                        )
                );
            }
        } catch (RateLimitedException e) {
            fail(e);
        }
    }
}

package io.ipinfo;

import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class IPInfoTest {
    @Test
    public void testGoogleDNS() {
        IPInfo ii = IPInfo.builder()
                .setToken(System.getenv("IPINFO_TOKEN"))
                .build();

        try {
            IPResponse response = ii.lookupIP("8.8.8.8");
            assertAll("8.8.8.8",
                    () -> assertEquals(response.getCountryCode(), "US"),
                    () -> assertEquals(response.getCountryName(), "United States"),
                    () -> assertEquals(response.getHostname(), "dns.google"),
                    () -> assertEquals(response.getTimezone(), "America/Los_Angeles"),
                    () -> assertEquals(response.getIp(), "8.8.8.8"),
                    () -> assertFalse(response.getPrivacy().getProxy()),
                    () -> assertFalse(response.getPrivacy().getHosting()),
                    () -> assertFalse(response.getPrivacy().getVpn()),
                    () -> assertFalse(response.getPrivacy().getTor()),
                    () -> assertEquals(response.getDomains().getDomains().size(), 5)
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testGetMap() {
        IPInfo ii = IPInfo.builder()
                .setToken(System.getenv("IPINFO_TOKEN"))
                .build();

        try {
            String mapUrl = ii.getMap(Arrays.asList("1.1.1.1", "2.2.2.2", "8.8.8.8"));
            System.out.println(mapUrl);
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testGetBatch() {
        IPInfo ii = IPInfo.builder()
                .setToken(System.getenv("IPINFO_TOKEN"))
                .build();

        try {
            List<String> urls = new ArrayList(10);
            urls.add("AS123");
            urls.add("8.8.8.8");
            urls.add("9.9.9.9/hostname");
            ConcurrentHashMap<String, Object> result = ii.getBatch(urls);

            assertAll("keys exist",
                    () -> assertTrue(result.containsKey("AS123")),
                    () -> assertTrue(result.containsKey("8.8.8.8")),
                    () -> assertTrue(result.containsKey("9.9.9.9/hostname"))
            );

            ASNResponse asnResp = (ASNResponse)result.get("AS123");
            assertAll("AS123",
                    () -> assertEquals(asnResp.getAsn(), "AS123"),
                    () -> assertEquals(asnResp.getName(), "Air Force Systems Networking"),
                    () -> assertEquals(asnResp.getCountryCode(), "US"),
                    () -> assertEquals(asnResp.getCountryName(), "United States"),
                    () -> assertEquals(asnResp.getAllocated(), "1987-08-24"),
                    () -> assertEquals(asnResp.getRegistry(), "arin"),
                    () -> assertEquals(asnResp.getDomain(), "af.mil"),
                    () -> assertEquals(asnResp.getNumIps(), new Integer(0)),
                    () -> assertEquals(asnResp.getType(), "inactive")
            );

            IPResponse ipResp = (IPResponse)result.get("8.8.8.8");
            assertAll("8.8.8.8",
                    () -> assertEquals(ipResp.getCountryCode(), "US"),
                    () -> assertEquals(ipResp.getCountryName(), "United States"),
                    () -> assertEquals(ipResp.getHostname(), "dns.google"),
                    () -> assertEquals(ipResp.getTimezone(), "America/Los_Angeles"),
                    () -> assertEquals(ipResp.getIp(), "8.8.8.8"),
                    () -> assertFalse(ipResp.getPrivacy().getProxy()),
                    () -> assertFalse(ipResp.getPrivacy().getHosting()),
                    () -> assertFalse(ipResp.getPrivacy().getVpn()),
                    () -> assertFalse(ipResp.getPrivacy().getTor()),
                    () -> assertEquals(ipResp.getDomains().getDomains().size(), 5)
            );

            String hostname = (String)result.get("9.9.9.9/hostname");
            assertEquals(hostname, "dns9.quad9.net");
        } catch (RateLimitedException e) {
            fail(e);
        }
    }
}

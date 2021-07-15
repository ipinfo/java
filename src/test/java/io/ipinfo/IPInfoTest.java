package io.ipinfo;

import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class IPInfoTest {
    @Test
    public void testGoogleDNS() {
        IPInfo ii = IPInfo.builder().build();

        try {
            IPResponse response = ii.lookupIP("8.8.8.8");
            System.out.println(response.toString());
            assertAll("Country Code",
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
        IPInfo ii = IPInfo.builder().build();

        try {
            String mapUrl = ii.getMap(Arrays.asList("1.1.1.1", "2.2.2.2", "8.8.8.8"));
            System.out.println(mapUrl);
        } catch (RateLimitedException e) {
            fail(e);
        }
    }
}

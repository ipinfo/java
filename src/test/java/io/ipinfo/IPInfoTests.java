package io.ipinfo;

import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IPInfoTests {
    private IPInfo ipInfo;

    @BeforeEach
    void initAll() {
        ipInfo = IPInfo.builder().build();
    }

    @Test
    void testGoogleDNS() {
        try {
            IPResponse response = ipInfo.lookupIP("8.8.8.8");

            assertAll("Country Code",
                    () -> assertEquals(response.getCountryCode(), "US"),
                    () -> assertEquals(response.getCountryName(), "United States"),
                    () -> assertEquals(response.getHostname(), "google-public-dns-a.google.com"),
                    () -> assertEquals(response.getIp(), "8.8.8.8")
            );

        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    void testASNWithoutAuth() {
        assertThrows(ErrorResponseException.class, () -> ipInfo.lookupASN("AS7922"));
    }
}

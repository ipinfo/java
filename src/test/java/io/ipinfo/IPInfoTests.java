package io.ipinfo;

import io.ipinfo.errors.ErrorResponseException;
import io.ipinfo.errors.RateLimitedException;
import io.ipinfo.model.IPResponse;
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
        assertThrows(ErrorResponseException.class, () -> {
            ipInfo.lookupASN("AS7922");
        });
    }
}

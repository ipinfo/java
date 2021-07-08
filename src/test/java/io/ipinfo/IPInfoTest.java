package io.ipinfo;

import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IPInfoTest {
    private IPInfo ipInfo;

    @Test
    public void testGoogleDNS() {
        ipInfo = IPInfo.builder().build();
        try {
            IPResponse response = ipInfo.lookupIP("8.8.8.8");
            System.out.println(response.toString());
            assertAll("Country Code",
                    () -> assertEquals(response.getCountryCode(), "US"),
                    () -> assertEquals(response.getCountryName(), "United States"),
                    () -> assertEquals(response.getHostname(), "dns.google"),
                    () -> assertEquals(response.getIp(), "8.8.8.8"),
                    () -> assertEquals(response.getPrivacy().getProxy(), false),
                    () -> assertEquals(response.getPrivacy().getHosting(), false),
                    () -> assertEquals(response.getPrivacy().getVpn(), false),
                    () -> assertEquals(response.getPrivacy().getTor(), false),
                    () -> assertEquals(response.getDomains().getDomains().size(), 5)
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }
}

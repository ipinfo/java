package io.ipinfo;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class IPinfoTest {
    @Test
    public void testGoogleDNS() {
        IPinfo ii = new IPinfo.Builder()
                .setToken(System.getenv("IPINFO_TOKEN"))
                .build();

        try {
            IPResponse response = ii.lookupIP("8.8.8.8");
            assertAll("8.8.8.8",
                    () -> assertEquals("8.8.8.8", response.getIp(), "IP mismatch"),
                    () -> assertEquals("dns.google", response.getHostname(), "hostname mismatch"),
                    () -> assertTrue(response.getAnycast(), "anycast mismatch"),
                    () -> assertEquals("Mountain View", response.getCity(), "city mismatch"),
                    () -> assertEquals("California", response.getRegion(), "region mismatch"),
                    () -> assertEquals("US", response.getCountryCode(), "country code mismatch"),
                    () -> assertEquals("United States", response.getCountryName(), "country name mismatch"),
                    () -> assertFalse(response.isEU(), "isEU mismatch"),
                    () -> assertEquals("🇺🇸", response.getCountryFlag().getEmoji(), "emoji mismatch"),
                    () -> assertEquals("U+1F1FA U+1F1F8", response.getCountryFlag().getUnicode(), "country flag unicode mismatch"),
                    () -> assertEquals("https://cdn.ipinfo.io/static/images/countries-flags/US.svg", response.getCountryFlagURL(), "country flag mismatch"),
                    () -> assertEquals("USD", response.getCountryCurrency().getCode(), "country currency code mismatch"),
                    () -> assertEquals("$", response.getCountryCurrency().getSymbol(), "country currency symbo mismatch"),
                    () -> assertEquals("NA", response.getContinent().getCode(), "continent code mismatch"),
                    () -> assertEquals("North America", response.getContinent().getName(), "continent name mismatch"),
                    () -> assertEquals("America/Los_Angeles", response.getTimezone(), "timezone mismatch"),
                    () -> assertFalse(response.getPrivacy().getProxy(), "proxy mismatch"),
                    () -> assertFalse(response.getPrivacy().getVpn(), "VPN mismatch"),
                    () -> assertFalse(response.getPrivacy().getTor(), "Tor mismatch"),
                    () -> assertFalse(response.getPrivacy().getRelay(), "relay mismatch"),
                    () -> assertTrue(response.getPrivacy().getHosting(), "hosting mismatch"),
                    () -> assertEquals("", response.getPrivacy().getService(), "service mismatch"),
                    () -> assertEquals(5, response.getDomains().getDomains().size(), "domains size mismatch")
            );

            IPResponse bogonResp = ii.lookupIP("2001:0:c000:200::0:255:1");
            assertAll("bogon response",
            () -> assertEquals("2001:0:c000:200::0:255:1", bogonResp.getIp(), "IP mismatch"),
            () -> assertTrue(bogonResp.getBogon(), "bogon mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Disabled
    @Test
    public void testGetMap() {
        IPinfo ii = new IPinfo.Builder()
                .setToken(System.getenv("IPINFO_TOKEN"))
                .build();

        try {
            String mapUrl = ii.getMap(Arrays.asList("1.1.1.1", "2.2.2.2", "8.8.8.8"));
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testGetBatch() {
        IPinfo ii = new IPinfo.Builder()
                .setToken(System.getenv("IPINFO_TOKEN"))
                .build();

        try {
            List<String> urls = new ArrayList(10);
            urls.add("AS123");
            urls.add("8.8.8.8");
            urls.add("9.9.9.9/hostname");
            urls.add("239.0.0.0");
            ConcurrentHashMap<String, Object> result = ii.getBatch(urls);

            assertAll("keys exist",
                    () -> assertTrue(result.containsKey("AS123"), "does not contain AS123"),
                    () -> assertTrue(result.containsKey("8.8.8.8"), "does not contain 8.8.8.8"),
                    () -> assertTrue(result.containsKey("9.9.9.9/hostname"), "does not contain 9.9.9.9/hostname")
            );

            ASNResponse asnResp = (ASNResponse)result.get("AS123");
            assertAll("AS123",
                    () -> assertEquals("AS123", asnResp.getAsn(), "ASN mismatch"),
                    () -> assertEquals("Air Force Systems Networking", asnResp.getName(), "name mismatch"),
                    () -> assertEquals("US", asnResp.getCountryCode(), "country code mismatch"),
                    () -> assertEquals("United States", asnResp.getCountryName(), "country name mismatch"),
                    () -> assertEquals("1987-08-24", asnResp.getAllocated(), "allocated mismatch"),
                    () -> assertEquals("arin", asnResp.getRegistry(), "registry mismatch"),
                    () -> assertEquals("af.mil", asnResp.getDomain(), "domain mismatch"),
                    () -> assertEquals(new Integer(0), asnResp.getNumIps(), "num IPs mismatch"),
                    () -> assertEquals("inactive", asnResp.getType(), "type mismatch")
            );

            IPResponse ipResp = (IPResponse)result.get("8.8.8.8");
            assertAll("8.8.8.8",
                    () -> assertEquals("8.8.8.8", ipResp.getIp(), "IP mismatch"),
                    () -> assertEquals("dns.google", ipResp.getHostname(), "hostname mismatch"),
                    () -> assertTrue(ipResp.getAnycast(), "anycast mismatch"),
                    () -> assertEquals("Mountain View", ipResp.getCity(), "city mismatch"),
                    () -> assertEquals("California", ipResp.getRegion(), "region mismatch"),
                    () -> assertEquals("US", ipResp.getCountryCode(), "country code mismatch"),
                    () -> assertEquals("United States", ipResp.getCountryName(), "country name mismatch"),
                    () -> assertEquals("America/Los_Angeles", ipResp.getTimezone(), "timezone mismatch"),
                    () -> assertFalse(ipResp.getPrivacy().getProxy(), "proxy mismatch"),
                    () -> assertFalse(ipResp.getPrivacy().getVpn(), "VPN mismatch"),
                    () -> assertFalse(ipResp.getPrivacy().getTor(), "Tor mismatch"),
                    () -> assertFalse(ipResp.getPrivacy().getRelay(), "relay mismatch"),
                    () -> assertTrue(ipResp.getPrivacy().getHosting(), "hosting mismatch"),
                    () -> assertEquals("", ipResp.getPrivacy().getService(), "service mismatch"),
                    () -> assertEquals(5, ipResp.getDomains().getDomains().size(), "domains size mismatch")
            );

            String hostname = (String)result.get("9.9.9.9/hostname");
            assertEquals("dns9.quad9.net", hostname, "hostname mismatch");

            IPResponse bogonResp = (IPResponse)result.get("239.0.0.0");
            assertAll("239.0.0.0",
                    () -> assertEquals("239.0.0.0", bogonResp.getIp(), "IP mismatch"),
                    () -> assertTrue(bogonResp.getBogon(), "bogon mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testGetBatchIps() {
        IPinfo ii = new IPinfo.Builder()
                .setToken(System.getenv("IPINFO_TOKEN"))
                .build();

        try {
            List<String> ips = new ArrayList(10);
            ips.add("1.1.1.1");
            ips.add("8.8.8.8");
            ips.add("9.9.9.9");
            ConcurrentHashMap<String, IPResponse> result = ii.getBatchIps(ips);

            assertAll("keys exist",
                    () -> assertTrue(result.containsKey("1.1.1.1"), "does not contain 1.1.1.1"),
                    () -> assertTrue(result.containsKey("8.8.8.8"), "does not contain 8.8.8.8"),
                    () -> assertTrue(result.containsKey("9.9.9.9"), "does not contain 9.9.9.9")
            );

            IPResponse res1 = result.get("1.1.1.1");
            assertAll("1.1.1.1",
                    () -> assertEquals("1.1.1.1", res1.getIp(), "IP mismatch"),
                    () -> assertEquals("one.one.one.one", res1.getHostname(), "hostname mismatch"),
                    () -> assertTrue(res1.getAnycast(), "anycast mismatch"),
                    () -> assertNotNull(res1.getCity(), "city should be set"),
                    () -> assertNotNull(res1.getRegion(), "region should be set"),
                    () -> assertNotNull(res1.getCountryCode(), "country code should be set"),
                    () -> assertNotNull(res1.getCountryName(), "country name should be set"),
                    () -> assertNotNull(res1.getTimezone(), "timezone should be set"),
                    () -> assertFalse(res1.getPrivacy().getProxy(), "proxy mismatch"),
                    () -> assertFalse(res1.getPrivacy().getVpn(), "VPN mismatch"),
                    () -> assertFalse(res1.getPrivacy().getTor(), "Tor mismatch"),
                    () -> assertFalse(res1.getPrivacy().getRelay(), "relay mismatch"),
                    () -> assertTrue(res1.getPrivacy().getHosting(), "hosting mismatch"),
                    () -> assertEquals("", res1.getPrivacy().getService(), "service mismatch"),
                    () -> assertEquals(5, res1.getDomains().getDomains().size(), "domains size mismatch")
            );

            IPResponse res2 = result.get("8.8.8.8");
            assertAll("8.8.8.8",
                    () -> assertEquals("8.8.8.8", res2.getIp(), "IP mismatch"),
                    () -> assertEquals("dns.google", res2.getHostname(), "hostname mismatch"),
                    () -> assertTrue(res2.getAnycast(), "anycast mismatch"),
                    () -> assertEquals("Mountain View", res2.getCity(), "city mismatch"),
                    () -> assertEquals("California", res2.getRegion(), "region mismatch"),
                    () -> assertEquals("US", res2.getCountryCode(), "country code mismatch"),
                    () -> assertEquals("United States", res2.getCountryName(), "country name mismatch"),
                    () -> assertEquals("America/Los_Angeles", res2.getTimezone(), "timezone mismatch"),
                    () -> assertFalse(res2.getPrivacy().getProxy(), "proxy mismatch"),
                    () -> assertFalse(res2.getPrivacy().getVpn(), "VPN mismatch"),
                    () -> assertFalse(res2.getPrivacy().getTor(), "Tor mismatch"),
                    () -> assertFalse(res2.getPrivacy().getRelay(), "relay mismatch"),
                    () -> assertTrue(res2.getPrivacy().getHosting(), "hosting mismatch"),
                    () -> assertEquals("", res2.getPrivacy().getService(), "service mismatch"),
                    () -> assertEquals(5, res2.getDomains().getDomains().size(), "domains size mismatch")
            );

            IPResponse res3 = result.get("9.9.9.9");
            assertAll("9.9.9.9",
                    () -> assertEquals("9.9.9.9", res3.getIp(), "IP mismatch"),
                    () -> assertEquals("dns9.quad9.net", res3.getHostname(), "hostname mismatch"),
                    () -> assertTrue(res3.getAnycast(), "anycast mismatch"),
                    () -> assertEquals("Ashburn", res3.getCity(), "city mismatch"),
                    () -> assertEquals("Virginia", res3.getRegion(), "region mismatch"),
                    () -> assertEquals("US", res3.getCountryCode(), "country code mismatch"),
                    () -> assertEquals("United States", res3.getCountryName(), "country name mismatch"),
                    () -> assertEquals("America/New_York", res3.getTimezone(), "timezone mismatch"),
                    () -> assertFalse(res3.getPrivacy().getProxy(), "proxy mismatch"),
                    () -> assertFalse(res3.getPrivacy().getVpn(), "VPN mismatch"),
                    () -> assertFalse(res3.getPrivacy().getTor(), "Tor mismatch"),
                    () -> assertFalse(res3.getPrivacy().getRelay(), "relay mismatch"),
                    () -> assertTrue(res3.getPrivacy().getHosting(), "hosting mismatch"),
                    () -> assertEquals("", res3.getPrivacy().getService(), "service mismatch"),
                    () -> assertEquals(5, res3.getDomains().getDomains().size(), "domains size mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testGetBatchAsns() {
        IPinfo ii = new IPinfo.Builder()
                .setToken(System.getenv("IPINFO_TOKEN"))
                .build();

        try {
            List<String> asns = new ArrayList(10);
            asns.add("AS123");
            asns.add("AS321");
            ConcurrentHashMap<String, ASNResponse> result = ii.getBatchAsns(asns);

            assertAll("keys exist",
                    () -> assertTrue(result.containsKey("AS123"), "does not contain AS123"),
                    () -> assertTrue(result.containsKey("AS321"), "does not contain AS321")
            );

            ASNResponse res1 = result.get("AS123");
            assertAll("AS123",
                    () -> assertEquals("AS123", res1.getAsn(), "ASN mismatch"),
                    () -> assertEquals("Air Force Systems Networking", res1.getName(), "name mismatch"),
                    () -> assertEquals("US", res1.getCountryCode(), "country code mismatch"),
                    () -> assertEquals("United States", res1.getCountryName(), "country name mismatch"),
                    () -> assertEquals("1987-08-24", res1.getAllocated(), "allocated mismatch"),
                    () -> assertEquals("arin", res1.getRegistry(), "registry mismatch"),
                    () -> assertEquals("af.mil", res1.getDomain(), "domain mismatch"),
                    () -> assertEquals(new Integer(0), res1.getNumIps(), "num IPs mismatch"),
                    () -> assertEquals("inactive", res1.getType(), "type mismatch")
            );

            ASNResponse res2 = result.get("AS321");
            assertAll("AS321",
                    () -> assertEquals("AS321", res2.getAsn(), "ASN mismatch"),
                    () -> assertNotNull(res2.getName(), "name should be set"),
                    () -> assertEquals("US", res2.getCountryCode(), "country code mismatch"),
                    () -> assertEquals("United States", res2.getCountryName(), "country name mismatch"),
                    () -> assertEquals("1989-06-30", res2.getAllocated(), "allocated mismatch"),
                    () -> assertEquals("arin", res2.getRegistry(), "registry mismatch"),
                    () -> assertEquals("mail.mil", res2.getDomain(), "domain mismatch"),
                    () -> assertNotNull(res2.getNumIps(), "num IPs should be set"),
                    () -> assertEquals("government", res2.getType(), "type mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }
}

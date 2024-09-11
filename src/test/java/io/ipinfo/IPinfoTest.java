package io.ipinfo;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;
import org.junit.jupiter.api.Test;

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
                    () -> assertEquals("8.8.8.8", response.getIp()),
                    () -> assertEquals("dns.google", response.getHostname()),
                    () -> assertTrue(response.getAnycast(), "Is anycast"),
                    () -> assertEquals("Mountain View", response.getCity()),
                    () -> assertEquals("California", response.getRegion()),
                    () -> assertEquals("US", response.getCountryCode()),
                    () -> assertEquals("United States", response.getCountryName()),
                    () -> assertFalse(response.isEU()),
                    () -> assertEquals("🇺🇸", response.getCountryFlag().getEmoji()),
                    () -> assertEquals("U+1F1FA U+1F1F8", response.getCountryFlag().getUnicode()),
                    () -> assertEquals("https://cdn.ipinfo.io/static/images/countries-flags/US.svg", response.getCountryFlagURL()),
                    () -> assertEquals("USD", response.getCountryCurrency().getCode()),
                    () -> assertEquals("$", response.getCountryCurrency().getSymbol()),
                    () -> assertEquals("NA", response.getContinent().getCode()),
                    () -> assertEquals("North America", response.getContinent().getName()),
                    () -> assertEquals("America/Los_Angeles", response.getTimezone()),
                    () -> assertFalse(response.getPrivacy().getProxy(), "Is proxy"),
                    () -> assertFalse(response.getPrivacy().getVpn(), "Is VPN"),
                    () -> assertFalse(response.getPrivacy().getTor(), "Is Tor"),
                    () -> assertFalse(response.getPrivacy().getRelay(), "Is relay"),
                    () -> assertTrue(response.getPrivacy().getHosting(), "Is hosting"),
                    () -> assertEquals("", response.getPrivacy().getService()),
                    () -> assertEquals(5, response.getDomains().getDomains().size())
            );

            IPResponse bogonResp = ii.lookupIP("2001:0:c000:200::0:255:1");
            assertAll("",
                    () -> assertEquals("2001:0:c000:200::0:255:1", bogonResp.getIp()),
                    () -> assertTrue(bogonResp.getBogon())
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

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
            List<String> urls = new ArrayList<>(10);
            urls.add("AS123");
            urls.add("8.8.8.8");
            urls.add("9.9.9.9/hostname");
            urls.add("239.0.0.0");
            ConcurrentHashMap<String, Object> result = ii.getBatch(urls);

            assertAll("keys exist",
                    () -> assertTrue(result.containsKey("AS123")),
                    () -> assertTrue(result.containsKey("8.8.8.8")),
                    () -> assertTrue(result.containsKey("9.9.9.9/hostname"))
            );

            ASNResponse asnResp = (ASNResponse) result.get("AS123");
            assertAll("AS123",
                    () -> assertEquals("AS123", asnResp.getAsn()),
                    () -> assertEquals("Air Force Systems Networking", asnResp.getName()),
                    () -> assertEquals("US", asnResp.getCountryCode()),
                    () -> assertEquals("United States", asnResp.getCountryName()),
                    () -> assertEquals("1987-08-24", asnResp.getAllocated()),
                    () -> assertEquals("arin", asnResp.getRegistry()),
                    () -> assertEquals("af.mil", asnResp.getDomain()),
                    () -> assertEquals(Integer.valueOf(0), asnResp.getNumIps()),
                    () -> assertEquals("inactive", asnResp.getType())
            );

            IPResponse ipResp = (IPResponse) result.get("8.8.8.8");
            assertAll("8.8.8.8",
                    () -> assertEquals("8.8.8.8", ipResp.getIp()),
                    () -> assertEquals("dns.google", ipResp.getHostname()),
                    () -> assertTrue(ipResp.getAnycast(), "Is anycast"),
                    () -> assertEquals("Mountain View", ipResp.getCity()),
                    () -> assertEquals("California", ipResp.getRegion()),
                    () -> assertEquals("US", ipResp.getCountryCode()),
                    () -> assertEquals("United States", ipResp.getCountryName()),
                    () -> assertEquals("America/Los_Angeles", ipResp.getTimezone()),
                    () -> assertFalse(ipResp.getPrivacy().getProxy(), "Is proxy"),
                    () -> assertFalse(ipResp.getPrivacy().getVpn(), "Is VPN"),
                    () -> assertFalse(ipResp.getPrivacy().getTor(), "Is Tor"),
                    () -> assertFalse(ipResp.getPrivacy().getRelay(), "Is relay"),
                    () -> assertTrue(ipResp.getPrivacy().getHosting(), "Is hosting"),
                    () -> assertEquals("", ipResp.getPrivacy().getService()),
                    () -> assertEquals(5, ipResp.getDomains().getDomains().size())
            );

            String hostname = (String) result.get("9.9.9.9/hostname");
            assertEquals("dns9.quad9.net", hostname);

            IPResponse bogonResp = (IPResponse) result.get("239.0.0.0");
            assertAll("239.0.0.0",
                    () -> assertEquals("239.0.0.0", bogonResp.getIp()),
                    () -> assertTrue(bogonResp.getBogon())
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
            List<String> ips = new ArrayList<>(10);
            ips.add("1.1.1.1");
            ips.add("8.8.8.8");
            ips.add("9.9.9.9");
            ConcurrentHashMap<String, IPResponse> result = ii.getBatchIps(ips);

            assertAll("keys exist",
                    () -> assertTrue(result.containsKey("1.1.1.1")),
                    () -> assertTrue(result.containsKey("8.8.8.8")),
                    () -> assertTrue(result.containsKey("9.9.9.9"))
            );

            IPResponse res1 = result.get("1.1.1.1");
            assertAll("1.1.1.1",
                    () -> assertEquals("1.1.1.1", res1.getIp()),
                    () -> assertEquals("one.one.one.one", res1.getHostname()),
                    () -> assertTrue(res1.getAnycast(), "Is anycast"),
                    () -> assertEquals("Jakarta", res1.getCity()),
                    () -> assertEquals("Jakarta", res1.getRegion()),
                    () -> assertEquals("ID", res1.getCountryCode()),
                    () -> assertEquals("Indonesia", res1.getCountryName()),
                    () -> assertEquals("Asia/Jakarta", res1.getTimezone()),
                    () -> assertFalse(res1.getPrivacy().getProxy(), "Is proxy"),
                    () -> assertFalse(res1.getPrivacy().getVpn(), "Is VPN"),
                    () -> assertFalse(res1.getPrivacy().getTor(), "Is Tor"),
                    () -> assertFalse(res1.getPrivacy().getRelay(), "Is relay"),
                    () -> assertTrue(res1.getPrivacy().getHosting(), "Is hosting"),
                    () -> assertEquals("", res1.getPrivacy().getService()),
                    () -> assertEquals(5, res1.getDomains().getDomains().size())
            );

            IPResponse res2 = result.get("8.8.8.8");
            assertAll("8.8.8.8",
                    () -> assertEquals("8.8.8.8", res2.getIp()),
                    () -> assertEquals("dns.google", res2.getHostname()),
                    () -> assertTrue(res2.getAnycast(), "Is anycast"),
                    () -> assertEquals("Mountain View", res2.getCity()),
                    () -> assertEquals("California", res2.getRegion()),
                    () -> assertEquals("US", res2.getCountryCode()),
                    () -> assertEquals("United States", res2.getCountryName()),
                    () -> assertEquals("America/Los_Angeles", res2.getTimezone()),
                    () -> assertFalse(res2.getPrivacy().getProxy(), "Is proxy"),
                    () -> assertFalse(res2.getPrivacy().getVpn(), "Is VPN"),
                    () -> assertFalse(res2.getPrivacy().getTor(), "Is Tor"),
                    () -> assertFalse(res2.getPrivacy().getRelay(), "Is relay"),
                    () -> assertTrue(res2.getPrivacy().getHosting(), "Is hosting"),
                    () -> assertEquals("", res2.getPrivacy().getService()),
                    () -> assertEquals(5, res2.getDomains().getDomains().size())

            );

            IPResponse res3 = result.get("9.9.9.9");
            assertAll("9.9.9.9",
                    () -> assertEquals("9.9.9.9", res3.getIp()),
                    () -> assertEquals("dns9.quad9.net", res3.getHostname()),
                    () -> assertTrue(res3.getAnycast(), "Is anycast"),
                    () -> assertEquals("Zürich", res3.getCity()),
                    () -> assertEquals("Zurich", res3.getRegion()),
                    () -> assertEquals("CH", res3.getCountryCode()),
                    () -> assertEquals("Switzerland", res3.getCountryName()),
                    () -> assertEquals("Europe/Zurich", res3.getTimezone()),
                    () -> assertFalse(res3.getPrivacy().getProxy(), "Is proxy"),
                    () -> assertFalse(res3.getPrivacy().getVpn(), "Is VPN"),
                    () -> assertFalse(res3.getPrivacy().getTor(), "Is Tor"),
                    () -> assertFalse(res3.getPrivacy().getRelay(), "Is relay"),
                    () -> assertTrue(res3.getPrivacy().getHosting(), "Is hosting"),
                    () -> assertEquals("", res3.getPrivacy().getService()),
                    () -> assertEquals(5, res3.getDomains().getDomains().size())

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
            List<String> asns = new ArrayList<>(10);
            asns.add("AS123");
            asns.add("AS321");
            ConcurrentHashMap<String, ASNResponse> result = ii.getBatchAsns(asns);

            assertAll("keys exist",
                    () -> assertTrue(result.containsKey("AS123")),
                    () -> assertTrue(result.containsKey("AS321"))
            );

            ASNResponse res1 = result.get("AS123");
            assertAll("AS123",
                    () -> assertEquals("AS123", res1.getAsn()),
                    () -> assertEquals("Air Force Systems Networking", res1.getName()),
                    () -> assertEquals("US", res1.getCountryCode()),
                    () -> assertEquals("United States", res1.getCountryName()),
                    () -> assertEquals("1987-08-24", res1.getAllocated()),
                    () -> assertEquals("arin", res1.getRegistry()),
                    () -> assertEquals("af.mil", res1.getDomain()),
                    () -> assertEquals(Integer.valueOf(0), res1.getNumIps()),
                    () -> assertEquals("inactive", res1.getType())
            );

            ASNResponse res2 = result.get("AS321");
            assertAll("AS321",
                    () -> assertEquals("AS321", res2.getAsn()),
                    () -> assertEquals("DoD Network Information Center", res2.getName()),
                    () -> assertEquals("US", res2.getCountryCode()),
                    () -> assertEquals("United States", res2.getCountryName()),
                    () -> assertEquals("1989-06-30", res2.getAllocated()),
                    () -> assertEquals("arin", res2.getRegistry()),
                    () -> assertEquals("mail.mil", res2.getDomain()),
                    () -> assertEquals(Integer.valueOf(66048), res2.getNumIps()),
                    () -> assertEquals("government", res2.getType())
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }
}

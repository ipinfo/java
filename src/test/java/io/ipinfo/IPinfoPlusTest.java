package io.ipinfo;

import static org.junit.jupiter.api.Assertions.*;

import io.ipinfo.api.IPinfoPlus;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponsePlus;
import org.junit.jupiter.api.Test;

public class IPinfoPlusTest {

    @Test
    public void testAccessToken() {
        String token = "test_token";
        IPinfoPlus client = new IPinfoPlus.Builder()
            .setToken(token)
            .build();
        assertNotNull(client);
    }

    @Test
    public void testGoogleDNS() {
        IPinfoPlus client = new IPinfoPlus.Builder()
            .setToken(System.getenv("IPINFO_TOKEN"))
            .build();

        try {
            IPResponsePlus response = client.lookupIP("8.8.8.8");
            assertAll(
                "8.8.8.8",
                () -> assertEquals("8.8.8.8", response.getIp(), "IP mismatch"),
                () -> assertEquals("dns.google", response.getHostname(), "hostname mismatch"),
                () -> assertNotNull(response.getGeo(), "geo should be set"),
                () -> assertEquals("Mountain View", response.getGeo().getCity(), "city mismatch"),
                () -> assertEquals("California", response.getGeo().getRegion(), "region mismatch"),
                () -> assertEquals("CA", response.getGeo().getRegionCode(), "region code mismatch"),
                () -> assertEquals("United States", response.getGeo().getCountry(), "country mismatch"),
                () -> assertEquals("US", response.getGeo().getCountryCode(), "country code mismatch"),
                () -> assertEquals("North America", response.getGeo().getContinent(), "continent mismatch"),
                () -> assertEquals("NA", response.getGeo().getContinentCode(), "continent code mismatch"),
                () -> assertNotNull(response.getGeo().getLatitude(), "latitude should be set"),
                () -> assertNotNull(response.getGeo().getLongitude(), "longitude should be set"),
                () -> assertEquals("America/Los_Angeles", response.getGeo().getTimezone(), "timezone mismatch"),
                () -> assertEquals("94043", response.getGeo().getPostalCode(), "postal code mismatch"),
                // Enriched fields
                () -> assertEquals("United States", response.getCountryName(), "country name mismatch"),
                () -> assertFalse(response.isEU(), "isEU mismatch"),
                () -> assertEquals("🇺🇸", response.getCountryFlag().getEmoji(), "emoji mismatch"),
                () -> assertEquals("U+1F1FA U+1F1F8", response.getCountryFlag().getUnicode(), "unicode mismatch"),
                () -> assertEquals("https://cdn.ipinfo.io/static/images/countries-flags/US.svg", response.getCountryFlagURL(), "flag URL mismatch"),
                () -> assertEquals("USD", response.getCountryCurrency().getCode(), "currency code mismatch"),
                () -> assertEquals("$", response.getCountryCurrency().getSymbol(), "currency symbol mismatch"),
                () -> assertEquals("NA", response.getContinentInfo().getCode(), "continent info code mismatch"),
                () -> assertEquals("North America", response.getContinentInfo().getName(), "continent info name mismatch"),
                // AS fields
                () -> assertNotNull(response.getAsn(), "asn should be set"),
                () -> assertEquals("AS15169", response.getAsn().getAsn(), "ASN mismatch"),
                () -> assertEquals("Google LLC", response.getAsn().getName(), "AS name mismatch"),
                () -> assertEquals("google.com", response.getAsn().getDomain(), "AS domain mismatch"),
                () -> assertEquals("hosting", response.getAsn().getType(), "AS type mismatch"),
                // Network flags
                () -> assertFalse(response.getIsAnonymous(), "is_anonymous mismatch"),
                () -> assertTrue(response.getIsAnycast(), "is_anycast mismatch"),
                () -> assertTrue(response.getIsHosting(), "is_hosting mismatch"),
                () -> assertFalse(response.getIsMobile(), "is_mobile mismatch"),
                () -> assertFalse(response.getIsSatellite(), "is_satellite mismatch"),
                // Plus-specific fields (may not be present depending on token tier)
                () -> assertFalse(response.getBogon(), "bogon mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testBogon() {
        IPinfoPlus client = new IPinfoPlus.Builder()
            .setToken(System.getenv("IPINFO_TOKEN"))
            .build();

        try {
            IPResponsePlus response = client.lookupIP("127.0.0.1");
            assertAll(
                "127.0.0.1",
                () -> assertEquals("127.0.0.1", response.getIp(), "IP mismatch"),
                () -> assertTrue(response.getBogon(), "bogon mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }

    @Test
    public void testCloudFlareDNS() {
        IPinfoPlus client = new IPinfoPlus.Builder()
            .setToken(System.getenv("IPINFO_TOKEN"))
            .build();

        try {
            IPResponsePlus response = client.lookupIP("1.1.1.1");
            assertAll(
                "1.1.1.1",
                () -> assertEquals("1.1.1.1", response.getIp(), "IP mismatch"),
                () -> assertEquals("one.one.one.one", response.getHostname(), "hostname mismatch"),
                () -> assertEquals("AS13335", response.getAsn().getAsn(), "ASN mismatch"),
                () -> assertEquals("Cloudflare, Inc.", response.getAsn().getName(), "AS name mismatch"),
                () -> assertEquals("cloudflare.com", response.getAsn().getDomain(), "AS domain mismatch"),
                () -> assertNotNull(response.getGeo().getCountryCode(), "country code should be set"),
                () -> assertNotNull(response.getGeo().getCountry(), "country should be set"),
                () -> assertNotNull(response.getCountryName(), "country name should be set"),
                () -> assertNotNull(response.getGeo().getContinentCode(), "continent code should be set"),
                () -> assertNotNull(response.getGeo().getContinent(), "continent should be set"),
                () -> assertNotNull(response.isEU(), "isEU should be set"),
                () -> assertNotNull(response.getCountryFlag().getEmoji(), "emoji should be set"),
                () -> assertNotNull(response.getCountryFlag().getUnicode(), "unicode should be set"),
                () -> assertNotNull(response.getCountryFlagURL(), "flag URL should be set"),
                () -> assertNotNull(response.getCountryCurrency().getCode(), "currency code should be set"),
                () -> assertNotNull(response.getCountryCurrency().getSymbol(), "currency symbol should be set"),
                () -> assertNotNull(response.getContinentInfo().getCode(), "continent info code should be set"),
                () -> assertNotNull(response.getContinentInfo().getName(), "continent info name should be set"),
                // Plus-specific fields (may not be present depending on token tier)
                () -> assertFalse(response.getBogon(), "bogon mismatch")
            );
        } catch (RateLimitedException e) {
            fail(e);
        }
    }
}

package io.ipinfo.api.model;

import com.google.gson.annotations.SerializedName;
import io.ipinfo.api.context.Context;

public class IPResponseCore {
    private final String ip;
    private final Geo geo;
    @SerializedName("as")
    private final ASN asn;
    private final Boolean is_anonymous;
    private final Boolean is_anycast;
    private final Boolean is_hosting;
    private final Boolean is_mobile;
    private final Boolean is_satellite;
    private final boolean bogon;
    private transient Context context;

    public IPResponseCore(
            String ip,
            Geo geo,
            ASN asn,
            Boolean is_anonymous,
            Boolean is_anycast,
            Boolean is_hosting,
            Boolean is_mobile,
            Boolean is_satellite
    ) {
        this.ip = ip;
        this.geo = geo;
        this.asn = asn;
        this.is_anonymous = is_anonymous;
        this.is_anycast = is_anycast;
        this.is_hosting = is_hosting;
        this.is_mobile = is_mobile;
        this.is_satellite = is_satellite;
        this.bogon = false;
    }

    public IPResponseCore(String ip, boolean bogon) {
        this.ip = ip;
        this.bogon = bogon;
        this.geo = null;
        this.asn = null;
        this.is_anonymous = null;
        this.is_anycast = null;
        this.is_hosting = null;
        this.is_mobile = null;
        this.is_satellite = null;
    }

    /**
     * Set by the library for extra utility functions
     *
     * @param context for country information
     */
    public void setContext(Context context) {
        this.context = context;
    }

    public String getIp() {
        return ip;
    }

    public Geo getGeo() {
        return geo;
    }

    public ASN getAsn() {
        return asn;
    }

    public Boolean getIsAnonymous() {
        return is_anonymous;
    }

    public Boolean getIsAnycast() {
        return is_anycast;
    }

    public Boolean getIsHosting() {
        return is_hosting;
    }

    public Boolean getIsMobile() {
        return is_mobile;
    }

    public Boolean getIsSatellite() {
        return is_satellite;
    }

    public boolean getBogon() {
        return bogon;
    }

    public String getCountryName() {
        return context != null && geo != null ? context.getCountryName(geo.getCountryCode()) : (geo != null ? geo.getCountry() : null);
    }

    public Boolean isEU() {
        return context != null && geo != null ? context.isEU(geo.getCountryCode()) : null;
    }

    public CountryFlag getCountryFlag() {
        return context != null && geo != null ? context.getCountryFlag(geo.getCountryCode()) : null;
    }

    public String getCountryFlagURL() {
        return context != null && geo != null ? context.getCountryFlagURL(geo.getCountryCode()) : null;
    }

    public CountryCurrency getCountryCurrency() {
        return context != null && geo != null ? context.getCountryCurrency(geo.getCountryCode()) : null;
    }

    public Continent getContinentInfo() {
        return context != null && geo != null ? context.getContinent(geo.getCountryCode()) : null;
    }

    @Override
    public String toString() {
        if (bogon) {
            return "IPResponseCore{" +
                    "ip='" + ip + '\'' +
                    ", bogon=" + bogon +
                    '}';
        }
        return "IPResponseCore{" +
                "ip='" + ip + '\'' +
                ", geo=" + geo +
                ", asn=" + asn +
                ", is_anonymous=" + is_anonymous +
                ", is_anycast=" + is_anycast +
                ", is_hosting=" + is_hosting +
                ", is_mobile=" + is_mobile +
                ", is_satellite=" + is_satellite +
                '}';
    }
}

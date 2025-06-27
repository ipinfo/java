package io.ipinfo.api.model;

import io.ipinfo.api.context.Context;

public class IPResponseLite {
    private final String ip;
    private final String asn;
    private final String as_name;
    private final String as_domain;
    private final String country_code;
    private final String country;
    private final String continent_code;
    private final String continent;
    private final boolean bogon;
    private transient Context context;

    public IPResponseLite(
            String ip,
            String asn,
            String as_name,
            String as_domain,
            String country_code,
            String country,
            String continent_code,
            String continent
    ) {
        this.ip = ip;
        this.asn = asn;
        this.as_name = as_name;
        this.as_domain = as_domain;
        this.country_code = country_code;
        this.country = country;
        this.continent_code = continent_code;
        this.continent = continent;
        this.bogon = false;
    }

    public IPResponseLite(String ip, boolean bogon) {
        this.ip = ip;
        this.bogon = bogon;
        this.asn = null;
        this.as_name = null;
        this.as_domain = null;
        this.country_code = null;
        this.country = null;
        this.continent_code = null;
        this.continent = null;
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

    public String getAsn() {
        return asn;
    }

    public String getAsName() {
        return as_name;
    }

    public String getAsDomain() {
        return as_domain;
    }

    public String getCountryCode() {
        return country_code;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryName() {
        return context != null ? context.getCountryName(getCountryCode()) : country;
    }

    public String getContinentCode() {
        return continent_code;
    }

    public String getContinent() {
        return continent;
    }

    public boolean getBogon() {
        return bogon;
    }

    public Boolean isEU() {
        return context != null ? context.isEU(getCountryCode()) : null;
    }

    public CountryFlag getCountryFlag() {
        return context != null ? context.getCountryFlag(getCountryCode()) : null;
    }

    public String getCountryFlagURL() {
        return context != null ? context.getCountryFlagURL(getCountryCode()) : null;
    }

    public CountryCurrency getCountryCurrency() {
        return context != null ? context.getCountryCurrency(getCountryCode()) : null;
    }

    public Continent getContinentInfo() {
        return context != null ? context.getContinent(getCountryCode()) : null;
    }

    @Override
    public String toString() {
        return (bogon ?
                "IPResponseLite{" +
                    "ip='" + ip + '\'' +
                    ", bogon=" + bogon +
                "}"
                :
                "IPResponseLite{" +
                    "ip='" + ip + '\'' +
                    ", asn='" + asn + '\'' +
                    ", as_name='" + as_name + '\'' +
                    ", as_domain='" + as_domain + '\'' +
                    ", country_code='" + country_code + '\'' +
                    ", country='" + country + '\'' +
                    ", continent_code='" + continent_code + '\'' +
                    ", continent='" + continent + '\'' +
                '}');
    }
}

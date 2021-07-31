package io.ipinfo.api.model;

import io.ipinfo.api.context.Context;

public class IPResponse {
    private final String ip;
    private final String hostname;
    private final boolean anycast;
    private final String city;
    private final String region;
    private final String country;
    private final String loc;
    private final String org;
    private final String postal;
    private final String timezone;
    private final ASN asn;
    private final Company company;
    private final Carrier carrier;
    private final Privacy privacy;
    private final Abuse abuse;
    private final Domains domains;
    private transient Context context;

    public IPResponse(
            String ip,
            String hostname,
            boolean anycast,
            String city,
            String region,
            String country,
            String loc,
            String org,
            String postal,
            String timezone,
            ASN asn,
            Company company,
            Carrier carrier,
            Privacy privacy,
            Abuse abuse,
            Domains domains
    ) {
        this.ip = ip;
        this.hostname = hostname;
        this.anycast = anycast;
        this.city = city;
        this.region = region;
        this.country = country;
        this.loc = loc;
        this.postal = postal;
        this.timezone = timezone;
        this.org = org;
        this.asn = asn;
        this.company = company;
        this.carrier = carrier;
        this.privacy = privacy;
        this.abuse = abuse;
        this.domains = domains;

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

    public String getHostname() {
        return hostname;
    }

    public boolean getAnycast() {
        return anycast;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountryCode() {
        return country;
    }

    public String getCountryName() {
        return context.getCountryName(getCountryCode());
    }

    public String getLocation() {
        return loc;
    }

    public String getLatitude() {
        try {
            return loc.split(",")[0];
        } catch (Exception ex) {
            return null;
        }
    }

    public String getLongitude() {
        try {
            return loc.split(",")[1];
        } catch (Exception ex) {
            return null;
        }
    }

    public String getOrg() {
        return org;
    }

    public String getPostal() {
        return postal;
    }

    public String getTimezone() {
        return timezone;
    }

    public ASN getAsn() {
        return asn;
    }

    public Company getCompany() {
        return company;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public Abuse getAbuse() {
        return abuse;
    }

    public Domains getDomains() {
        return domains;
    }

    @Override
    public String toString() {
        return "IPResponse{" +
                "ip='" + ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", anycast=" + anycast +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", loc='" + loc + '\'' +
                ", org='" + org + '\'' +
                ", postal='" + postal + '\'' +
                ", timezone='" + timezone + '\'' +
                ", asn=" + asn +
                ", company=" + company +
                ", carrier=" + carrier +
                ", privacy=" + privacy +
                ", abuse=" + abuse +
                ", domains=" + domains +
                '}';
    }
}

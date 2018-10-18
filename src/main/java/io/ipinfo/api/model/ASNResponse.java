package io.ipinfo.api.model;


import com.google.gson.annotations.SerializedName;
import io.ipinfo.api.context.Context;

import java.util.List;

public class ASNResponse {
    private final String asn;
    private final String name;
    private final String country;
    private final String allocated;
    private final String registry;
    private final String domain;
    @SerializedName("num_ips")
    private final String numIps;
    private final List<Prefix> prefixes;
    private final List<Prefix> prefixes6;
    private transient Context context;

    public ASNResponse(String asn, String name, String country, String allocated, String registry, String domain, String numIps, List<Prefix> prefixes, List<Prefix> prefixes6) {
        this.asn = asn;
        this.name = name;
        this.country = country;
        this.allocated = allocated;
        this.registry = registry;
        this.domain = domain;
        this.numIps = numIps;
        this.prefixes = prefixes;
        this.prefixes6 = prefixes6;
    }

    /**
     * Set by the library for extra utility functions
     *
     * @param context for country information
     */
    public void setContext(Context context) {
        this.context = context;
    }

    public String getAsn() {
        return asn;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getAllocated() {
        return allocated;
    }

    public String getRegistry() {
        return registry;
    }

    public String getDomain() {
        return domain;
    }

    public String getNumIps() {
        return numIps;
    }

    public List<Prefix> getPrefixes() {
        return prefixes;
    }

    public List<Prefix> getPrefixes6() {
        return prefixes6;
    }

    @Override
    public String toString() {
        return "ASNResponse{" +
                "asn='" + asn + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", allocated='" + allocated + '\'' +
                ", registry='" + registry + '\'' +
                ", domain='" + domain + '\'' +
                ", numIps='" + numIps + '\'' +
                ", prefixes=" + prefixes +
                ", prefixes6=" + prefixes6 +
                '}';
    }
}

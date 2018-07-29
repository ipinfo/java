package io.ipinfo.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AsnResponse {
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

    public AsnResponse(String asn, String name, String country, String allocated, String registry, String domain, String numIps, List<Prefix> prefixes, List<Prefix> prefixes6) {
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

    @Override
    public String toString() {
        return "AsnResponse{" +
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

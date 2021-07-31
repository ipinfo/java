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
    private final Integer numIps;
    private final String type;
    private final List<Prefix> prefixes;
    private final List<Prefix> prefixes6;
    private final List<String> peers;
    private final List<String> upstreams;
    private final List<String> downstreams;
    private transient Context context;

    public ASNResponse(
            String asn,
            String name,
            String country,
            String allocated,
            String registry,
            String domain,
            Integer numIps,
            String type,
            List<Prefix> prefixes,
            List<Prefix> prefixes6,
            List<String> peers,
            List<String> upstreams,
            List<String> downstreams
    ) {
        this.asn = asn;
        this.name = name;
        this.country = country;
        this.allocated = allocated;
        this.registry = registry;
        this.domain = domain;
        this.numIps = numIps;
        this.type = type;
        this.prefixes = prefixes;
        this.prefixes6 = prefixes6;
        this.peers = peers;
        this.upstreams = upstreams;
        this.downstreams = downstreams;
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

    public String getCountryCode() {
        return country;
    }

    public String getCountryName() {
        return context.getCountryName(getCountryCode());
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

    public Integer getNumIps() {
        return numIps;
    }

    public String getType() {
        return type;
    }

    public List<Prefix> getPrefixes() {
        return prefixes;
    }

    public List<Prefix> getPrefixes6() {
        return prefixes6;
    }

    public List<String> getPeers() {
        return peers;
    }

    public List<String> getUpstreams() {
        return upstreams;
    }

    public List<String> getDownstreams() {
        return downstreams;
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
                ", type='" + type + '\'' +
                ", prefixes=" + prefixes +
                ", prefixes6=" + prefixes6 +
                ", peers=" + peers +
                ", upstreams=" + upstreams +
                ", downstreams=" + downstreams +
                '}';
    }
}

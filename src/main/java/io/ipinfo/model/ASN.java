package io.ipinfo.model;


public class ASN {
    private final String asn;
    private final String name;
    private final String domain;
    private final String route;
    private final String type;

    public ASN(String asn, String name, String domain, String route, String type) {
        this.asn = asn;
        this.name = name;
        this.domain = domain;
        this.route = route;
        this.type = type;
    }

    public String getAsn() {
        return asn;
    }

    public String getName() {

        return name;
    }

    public String getDomain() {
        return domain;
    }

    public String getRoute() {
        return route;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ASN{" +
                "asn='" + asn + '\'' +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", route='" + route + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

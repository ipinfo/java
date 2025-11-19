package io.ipinfo.api.model;

public class ASNPlus {
    private final String asn;
    private final String name;
    private final String domain;
    private final String type;
    private final String last_changed;

    public ASNPlus(
            String asn,
            String name,
            String domain,
            String type,
            String last_changed
    ) {
        this.asn = asn;
        this.name = name;
        this.domain = domain;
        this.type = type;
        this.last_changed = last_changed;
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

    public String getType() {
        return type;
    }

    public String getLastChanged() {
        return last_changed;
    }

    @Override
    public String toString() {
        return "ASNPlus{" +
                "asn='" + asn + '\'' +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", type='" + type + '\'' +
                ", last_changed='" + last_changed + '\'' +
                '}';
    }
}

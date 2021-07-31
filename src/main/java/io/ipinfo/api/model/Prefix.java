package io.ipinfo.api.model;

public class Prefix {
    private final String netblock;
    private final String id;
    private final String name;
    private final String country;
    private final String size;
    private final String status;
    private final String domain;

    public Prefix(
            String netblock,
            String id,
            String name,
            String country,
            String size,
            String status,
            String domain
    ) {
        this.netblock = netblock;
        this.id = id;
        this.name = name;
        this.country = country;
        this.size = size;
        this.status = status;
        this.domain = domain;
    }

    public String getNetblock() {
        return netblock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getSize() {
        return size;
    }

    public String getStatus() {
        return status;
    }

    public String getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return "Prefix{" +
                "netblock='" + netblock + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", size='" + size + '\'' +
                ", status='" + status + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}

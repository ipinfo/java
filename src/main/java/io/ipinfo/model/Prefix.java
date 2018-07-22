package io.ipinfo.model;

public class Prefix {
    private final String netblock;
    private final String id;
    private final String name;
    private final String country;

    public Prefix(String netblock, String id, String name, String country) {
        this.netblock = netblock;
        this.id = id;
        this.name = name;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Prefix{" +
                "netblock='" + netblock + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}

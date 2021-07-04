package io.ipinfo.api.model;

import java.util.List;

public class Domains {
    private final String total;
    private final List<String> domains;

    public Domains( String total, List<String> domains) {
        this.total = total;
        this.domains = domains;
    }

    @Override
    public String toString() {
        return "Domains{" +
                "total='" + total + '\'' +
                ",domains='" + domains + '\'' +
                '}';
    }

    public String getTotal() {
        return total;
    }

    public List<String> getDomains() {
        return domains;
    }
}

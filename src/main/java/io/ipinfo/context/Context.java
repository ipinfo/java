package io.ipinfo.context;

import java.util.Map;

public class Context {
    private final Map<String, String> countryMap;

    public Context(Map<String, String> countryMap) {
        this.countryMap = countryMap;
    }

    public String getCountryName(String countryCode) {
        return countryMap.get(countryCode);
    }
}

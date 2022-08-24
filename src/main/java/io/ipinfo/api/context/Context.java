package io.ipinfo.api.context;

import java.util.List;
import java.util.Map;

public class Context {
    private final Map<String, String> countryMap;
    private final List<String> euCountries;

    public Context(Map<String, String> countryMap, List<String> euCountries) {
        this.countryMap = countryMap;
        this.euCountries = euCountries;
    }

    public String getCountryName(String countryCode) {
        return countryMap.get(countryCode);
    }

    public boolean isEU(String countryCode) {
        return euCountries.contains(countryCode);
    }
}

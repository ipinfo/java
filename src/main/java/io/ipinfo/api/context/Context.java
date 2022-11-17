package io.ipinfo.api.context;

import java.util.List;
import java.util.Map;

import io.ipinfo.api.model.Continent;
import io.ipinfo.api.model.CountryCurrency;
import io.ipinfo.api.model.CountryFlag;

public class Context {
    private final Map<String, String> countryMap;
    private final Map<String, CountryFlag> countriesFlags;
    private final Map<String, CountryCurrency> countriesCurrencies;
    private final Map<String, Continent> continents;
    private final List<String> euCountries;

    public Context(
        Map<String, String> countryMap, 
        List<String> euCountries, 
        Map<String, CountryFlag> countriesFlags, 
        Map<String, CountryCurrency> countriesCurrencies,
        Map<String, Continent> continents) 
        {
            this.countryMap = countryMap;
            this.euCountries = euCountries;
            this.countriesFlags = countriesFlags;
            this.countriesCurrencies = countriesCurrencies;
            this.continents = continents;
        }

    public String getCountryName(String countryCode) {
        return countryMap.get(countryCode);
    }

    public CountryFlag getCountryFlag(String countryCode) {
        return countriesFlags.get(countryCode);
    }

    public CountryCurrency getCountryCurrency(String countryCode) {
        return countriesCurrencies.get(countryCode);
    }

    public Continent getContinent(String countryCode) {
        return continents.get(countryCode);
    }

    public boolean isEU(String countryCode) {
        return euCountries.contains(countryCode);
    }
}

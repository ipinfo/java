package io.ipinfo.api.model;

public class Geo {
    private final String city;
    private final String region;
    private final String region_code;
    private final String country;
    private final String country_code;
    private final String continent;
    private final String continent_code;
    private final Double latitude;
    private final Double longitude;
    private final String timezone;
    private final String postal_code;

    public Geo(
            String city,
            String region,
            String region_code,
            String country,
            String country_code,
            String continent,
            String continent_code,
            Double latitude,
            Double longitude,
            String timezone,
            String postal_code
    ) {
        this.city = city;
        this.region = region;
        this.region_code = region_code;
        this.country = country;
        this.country_code = country_code;
        this.continent = continent;
        this.continent_code = continent_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.postal_code = postal_code;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getRegionCode() {
        return region_code;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return country_code;
    }

    public String getContinent() {
        return continent;
    }

    public String getContinentCode() {
        return continent_code;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getPostalCode() {
        return postal_code;
    }

    @Override
    public String toString() {
        return "Geo{" +
                "city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", region_code='" + region_code + '\'' +
                ", country='" + country + '\'' +
                ", country_code='" + country_code + '\'' +
                ", continent='" + continent + '\'' +
                ", continent_code='" + continent_code + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone='" + timezone + '\'' +
                ", postal_code='" + postal_code + '\'' +
                '}';
    }
}

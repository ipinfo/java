package io.ipinfo.model;


public class IPResponse {
    private final String ip;
    private final String hostname;
    private final String city;
    private final String region;
    private final String country;
    private final String loc;
    private final String postal;
    private final String org;
    private final String phone;
    private final Asn asn;
    private final Company company;
    private final Carrier carrier;

    public IPResponse(String ip, String hostname, String city, String region, String country, String loc, String postal, String org, String phone, Asn asn, Company company, Carrier carrier) {
        this.ip = ip;
        this.hostname = hostname;
        this.city = city;
        this.region = region;
        this.country = country;
        this.loc = loc;
        this.postal = postal;
        this.org = org;
        this.phone = phone;
        this.asn = asn;
        this.company = company;
        this.carrier = carrier;
    }

    @Override
    public String toString() {
        return "IPResponse{" +
                "ip='" + ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", loc='" + loc + '\'' +
                ", postal='" + postal + '\'' +
                ", org='" + org + '\'' +
                ", phone='" + phone + '\'' +
                ", asn=" + asn +
                ", company=" + company +
                ", carrier=" + carrier +
                '}';
    }
}

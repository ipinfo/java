package io.ipinfo.api.model;

public class Abuse {
    private final String address;
    private final String country;
    private final String email;
    private final String name;
    private final String network;
    private final String phone;

    public Abuse(
            String address,
            String country,
            String email,
            String name,
            String network,
            String phone
    ) {
        this.address = address;
        this.country = country;
        this.email = email;
        this.name = name;
        this.network = network;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNetwork() {
        return network;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Abuse{" +
                "address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", network='" + network + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

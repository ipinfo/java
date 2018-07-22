package io.ipinfo.model;


public class Carrier {
    private final String name;
    private final String mcc;
    private final String mnc;

    public Carrier(String name, String mcc, String mnc) {
        this.name = name;
        this.mcc = mcc;
        this.mnc = mnc;
    }

    @Override
    public String toString() {
        return "Carrier{" +
                "name='" + name + '\'' +
                ", mcc='" + mcc + '\'' +
                ", mnc='" + mnc + '\'' +
                '}';
    }
}

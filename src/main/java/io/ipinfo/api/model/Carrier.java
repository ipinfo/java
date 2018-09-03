package io.ipinfo.api.model;


public class Carrier {
    private final String name;
    private final String mcc;
    private final String mnc;

    public Carrier(String name, String mcc, String mnc) {
        this.name = name;
        this.mcc = mcc;
        this.mnc = mnc;
    }

    public String getName() {
        return name;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMnc() {
        return mnc;
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

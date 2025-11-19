package io.ipinfo.api.model;

public class Anonymous {
    private final Boolean is_proxy;
    private final Boolean is_relay;
    private final Boolean is_tor;
    private final Boolean is_vpn;

    public Anonymous(
            Boolean is_proxy,
            Boolean is_relay,
            Boolean is_tor,
            Boolean is_vpn
    ) {
        this.is_proxy = is_proxy;
        this.is_relay = is_relay;
        this.is_tor = is_tor;
        this.is_vpn = is_vpn;
    }

    public Boolean getIsProxy() {
        return is_proxy;
    }

    public Boolean getIsRelay() {
        return is_relay;
    }

    public Boolean getIsTor() {
        return is_tor;
    }

    public Boolean getIsVpn() {
        return is_vpn;
    }

    @Override
    public String toString() {
        return "Anonymous{" +
                "is_proxy=" + is_proxy +
                ", is_relay=" + is_relay +
                ", is_tor=" + is_tor +
                ", is_vpn=" + is_vpn +
                '}';
    }
}

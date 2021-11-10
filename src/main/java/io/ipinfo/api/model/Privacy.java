package io.ipinfo.api.model;

public class Privacy {
    private final boolean vpn;
    private final boolean proxy;
    private final boolean tor;
    private final boolean relay;
    private final boolean hosting;
    private final String service;

    public Privacy(
            boolean vpn,
            boolean proxy,
            boolean tor,
            boolean relay,
            boolean hosting,
            String service
    ) {
        this.vpn = vpn;
        this.proxy = proxy;
        this.tor = tor;
        this.relay = relay;
        this.hosting = hosting;
        this.service = service;
    }

    public boolean getVpn() {
        return vpn;
    }

    public boolean getProxy() {
        return proxy;
    }

    public boolean getTor() {
        return tor;
    }

    public boolean getRelay() {
        return relay;
    }

    public boolean getHosting() {
        return hosting;
    }

    public String getService() {
        return service;
    }

    @Override
    public String toString() {
        return "Privacy{" +
                "vpn=" + vpn +
                ", proxy=" + proxy +
                ", tor=" + tor +
                ", relay=" + relay +
                ", hosting=" + hosting +
                ", service=" + service +
                '}';
    }
}

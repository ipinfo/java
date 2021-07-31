package io.ipinfo.api.model;

public class Privacy {
    private final boolean vpn;
    private final boolean proxy;
    private final boolean tor;
    private final boolean hosting;

    public Privacy(
            boolean vpn,
            boolean proxy,
            boolean tor,
            boolean hosting
    ) {
        this.vpn = vpn;
        this.proxy = proxy;
        this.tor = tor;
        this.hosting = hosting;
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

    public boolean getHosting() {
        return hosting;
    }

    @Override
    public String toString() {
        return "Privacy{" +
                "vpn=" + vpn +
                ", proxy=" + proxy +
                ", tor=" + tor +
                ", hosting=" + hosting +
                '}';
    }
}

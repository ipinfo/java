package io.ipinfo.api.cache;


import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;

public class NoCache implements Cache {
    @Override
    public IPResponse getIp(String ip) {
        return null;
    }

    @Override
    public ASNResponse getAsn(String asn) {
        return null;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public boolean setIp(String ip, IPResponse response) {
        return false;
    }

    @Override
    public boolean setAsn(String asn, ASNResponse response) {
        return false;
    }

    @Override
    public boolean set(String key, Object val) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }
}

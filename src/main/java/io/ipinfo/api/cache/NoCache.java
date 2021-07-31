package io.ipinfo.api.cache;

public class NoCache implements Cache {
    @Override
    public Object get(String key) {
        return null;
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

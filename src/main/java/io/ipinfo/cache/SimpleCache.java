package io.ipinfo.cache;

import io.ipinfo.model.ASNResponse;
import io.ipinfo.model.IPResponse;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

public class SimpleCache implements Cache {
    private final Duration duration;
    private final Map<String, Payload<ASNResponse>> asnCache = new HashMap<>();
    private final Map<String, Payload<IPResponse>> ipCache = new HashMap<>();

    public SimpleCache(Duration duration) {
        this.duration = duration;
    }

    @Override
    public IPResponse getIp(String ip) {
        Payload<IPResponse> payload = ipCache.get(ip);
        if (payload == null || payload.hasExpired()) return null;

        return payload.data;
    }

    @Override
    public ASNResponse getAsn(String asn) {
        Payload<ASNResponse> payload = asnCache.get(asn);
        if (payload == null || payload.hasExpired()) return null;

        return payload.data;
    }

    @Override
    public boolean setIp(String ip, IPResponse response) {
        ipCache.put(ip, new Payload<>(response, duration));
        return true;
    }

    @Override
    public boolean setAsn(String asn, ASNResponse response) {
        asnCache.put(asn, new Payload<>(response, duration));
        return true;
    }

    @Override
    public boolean clear() {
        ipCache.clear();
        asnCache.clear();
        return true;
    }


    private class Payload<T> {
        final T data;
        final Instant creation;
        final Duration expiration;

        Payload(T data, Duration duration) {
            this.data = data;
            creation = Instant.now();
            this.expiration = duration;

        }

        public boolean hasExpired() {
            long time = expiration.addTo(creation).getLong(ChronoField.INSTANT_SECONDS);
            long now = System.currentTimeMillis();

            if (now > time) return false;
            return true;
        }

        public T getData() {
            return data;
        }
    }
}

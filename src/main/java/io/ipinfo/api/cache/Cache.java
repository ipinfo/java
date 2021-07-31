package io.ipinfo.api.cache;

import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;

public interface Cache {
    /**
     * Gets an arbitrary object stored in cache.
     *
     * @param key the key that maps to the object.
     * @return the value mapped to from `key`.
     */
    Object get(String key);

    /**
     * Sets a key/value pair in the cache.
     *
     * @param key the key.
     * @param val the value.
     * @return if caching was successful.
     */
    boolean set(String key, Object val);

    /**
     * Clears all entries in the cache.
     *
     * @return if clear was successful.
     */
    boolean clear();
}

package io.ipinfo.api.cache;

import io.ipinfo.api.model.ASNResponse;
import io.ipinfo.api.model.IPResponse;

public interface Cache {
    /**
     * Gets the IPResponse stored in cache.
     *
     * @param ip The ip string
     * @return IPResponse Object - may be null.
     */
    IPResponse getIp(String ip);

    /**
     * Gets the ASNResponse stored in cache.
     *
     * @param asn The asn string
     * @return ASNResponse Object - may be null.
     */
    ASNResponse getAsn(String asn);

    /**
     * Gets an arbitrary object stored in cache.
     *
     * @param key the key that maps to the object.
     * @return the value mapped to from `key`.
     */
    Object get(String key);

    /**
     * Sets the IP in the cache.
     *
     * @param ip       The IP string.
     * @param response The IPResponse object - may be null.
     * @return if caching was successful
     */
    boolean setIp(String ip, IPResponse response);

    /**
     * Sets the ASN in the cache.
     *
     * @param asn      the ASN string.
     * @param response the ASNResponse object - may be null.
     * @return if caching was successful.
     */
    boolean setAsn(String asn, ASNResponse response);

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

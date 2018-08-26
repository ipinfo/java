package io.ipinfo.cache;

import io.ipinfo.model.ASNResponse;
import io.ipinfo.model.IPResponse;

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
     * Sets the IP in the cache.
     *
     * @param ip       The IP string.
     * @param response The IPResponse object - may be null.
     * @return if cache was successful
     */
    boolean setIp(String ip, IPResponse response);

    /**
     * Sets the ASN in the cache.
     *
     * @param asn      the ASN string.
     * @param response the ASNResponse object - may be null.
     * @return if cache was successful.
     */
    boolean setAsn(String asn, ASNResponse response);

    /**
     * Clears all entries in the cache.
     * @return if clear was successful.
     */
    boolean clear();
}

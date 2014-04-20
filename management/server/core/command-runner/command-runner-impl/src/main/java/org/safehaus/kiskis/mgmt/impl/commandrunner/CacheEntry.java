/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safehaus.kiskis.mgmt.impl.commandrunner;

/**
 * This class represents entry for {@code ExpiringCache}. Holds generic value
 * for the specified ttl.
 *
 * @author dilshat
 */
class CacheEntry<ValueType> {

    /**
     * entry value
     */
    private final ValueType value;
    /**
     * creation timestamp
     */
    private final long createTimestamp;
    /**
     * time-to-live for this value
     */
    private final long ttlMs;

    /**
     * Initializes {@code CacheEntry} with the give type and ttl
     *
     * @param value - entry value
     * @param ttlMs - entry time to live in milliseconds
     */
    public CacheEntry(ValueType value, long ttlMs) {
        this.value = value;
        this.ttlMs = ttlMs;
        this.createTimestamp = System.currentTimeMillis();
    }

    /**
     * Return entry value
     *
     * @return entry value
     */
    public ValueType getValue() {
        return value;
    }

    /**
     * Returns boolean indicating if entry has expired. Entry is considered to
     * be expired if specified {@code ttl} has passed since the moment of entry
     * creation
     *
     * @return boolean indicating if entry has expired
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > createTimestamp + ttlMs;
    }

}

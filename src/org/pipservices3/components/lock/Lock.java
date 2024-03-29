package org.pipservices3.components.lock;

import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IReconfigurable;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.errors.ConflictException;

/**
 * Abstract lock that implements default lock acquisition routine.
 * <p>
 * ### Configuration parameters ###
 * <p>
 * <ul>
 * - options:
 * <li> - retry_timeout:   timeout in milliseconds to retry lock acquisition. (Default: 100)
 * </ul>
 *
 * @see ILock
 */
public abstract class Lock implements ILock, IReconfigurable {

    private int _retryTimeout = 100;

    /**
     * Configures component by passing configuration parameters.
     *
     * @param config configuration parameters to be set.
     */
    public void configure(ConfigParams config) {
        this._retryTimeout = config.getAsIntegerWithDefault("options.retry_timeout", _retryTimeout);
    }

    /**
     * Makes a single attempt to acquire a lock by its key.
     * It returns immediately a positive or negative result.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique lock key to acquire.
     * @param ttl           a lock timeout (time to live) in milliseconds.
     * @return <code>true</code> if the lock was acquired and <code>false</code> otherwise.
     */
    public abstract boolean tryAcquireLock(String correlationId, String key, int ttl);

    /**
     * Makes multiple attempts to acquire a lock by its key within give time interval.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique lock key to acquire.
     * @param ttl           a lock timeout (time to live) in milliseconds.
     * @param timeout       a lock acquisition timeout.
     */
    public void acquireLock(String correlationId, String key, int ttl, long timeout) throws InterruptedException, ApplicationException {
        long retryTime = System.currentTimeMillis() + timeout;

        // Try to get lock first
        boolean ok = this.tryAcquireLock(correlationId, key, ttl);
        if (ok)
            return;

        // Start retrying
        while (!ok) {
            // Sleep for a while...
            Thread.sleep(this._retryTimeout);

            // When timeout expires return false
            long now = System.currentTimeMillis();
            if (now > retryTime) {
                throw new ConflictException(
                        correlationId,
                        "LOCK_TIMEOUT",
                        "Acquiring lock " + key + " failed on timeout"
                ).withDetails("key", key);
            }

            ok = this.tryAcquireLock(correlationId, key, ttl);
        }
    }

    /**
     * Releases prevously acquired lock by its key.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique lock key to release.
     */
    public abstract void releaseLock(String correlationId, String key);
}

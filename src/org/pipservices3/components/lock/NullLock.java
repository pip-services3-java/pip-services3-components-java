package org.pipservices3.components.lock;

/**
 * Dummy lock implementation that doesn't do anything.
 * <p>
 * It can be used in testing or in situations when lock is required
 * but shall be disabled.
 *
 * @see ILock
 */
public class NullLock implements ILock {
    /**
     * Makes a single attempt to acquire a lock by its key.
     * It returns immediately a positive or negative result.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique lock key to acquire.
     * @param ttl           a lock timeout (time to live) in milliseconds.
     * @return <code>true</code> if the lock was acquired and <code>false</code> otherwise.
     */
    @Override
    public boolean tryAcquireLock(String correlationId, String key, int ttl) {
        return false;
    }

    /**
     * Makes multiple attempts to acquire a lock by its key within give time interval.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique lock key to acquire.
     * @param ttl           a lock timeout (time to live) in milliseconds.
     * @param timeout       a lock acquisition timeout.
     */
    @Override
    public void acquireLock(String correlationId, String key, int ttl, long timeout) {
        // Do nothing...
    }

    /**
     * Releases prevously acquired lock by its key.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param key           a unique lock key to release.
     */
    @Override
    public void releaseLock(String correlationId, String key) {
        // Do nothing...
    }
}

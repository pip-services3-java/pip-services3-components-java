package org.pipservices3.components.cache;

/**
 * Interface for caches that are used to cache values to improve performance. 
 */
public interface ICache {
	/**
	 * Retrieves cached value from the cache using its key. If value is missing in
	 * the cache or expired it returns null.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a unique value key.
	 * @return a cached value or null if value wasn't found or timeout expired.
	 */
	Object retrieve(String correlationId, String key);

	/**
	 * Stores value in the cache with expiration time.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a unique value key.
	 * @param value         a value to store.
	 * @param timeout       expiration timeout in milliseconds.
	 * @return a cached value stored in the cache.
	 */
	Object store(String correlationId, String key, Object value, long timeout);

	/**
	 * Removes a value from the cache by its key.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a unique value key.
	 */
	void remove(String correlationId, String key);
}

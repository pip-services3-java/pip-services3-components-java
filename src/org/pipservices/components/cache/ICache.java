package org.pipservices.components.cache;

/**
 * Transient cache which is used to bypass persistence 
 * to increase overall system performance. 
 */
public interface ICache {
	/**
	 * Retrieves a value from the cache by unique key.
	 * It is recommended to use either string GUIDs like '123456789abc'
	 * or unique natural keys prefixed with the functional group
	 * like 'pip-services-storage:block-123'. 
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache
	 * @return a cached value or <b>null</b> if value wasn't found or timeout expired.
	 */
	Object retrieve(String correlationId, String key);
	
	/**
	 * Stores value identified by unique key in the cache. 
	 * Stale timeout is configured in the component options. 
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache.
	 * @param value a value to store.
	 * @param timeout time for value to live in milliseconds
	 * @return a cached value stored in the cache.
	 */
	Object store(String correlationId, String key, Object value, long timeout);
	
	/**
	 * Removes value stored in the cache.
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache.
	 */
	void remove(String correlationId, String key);
}

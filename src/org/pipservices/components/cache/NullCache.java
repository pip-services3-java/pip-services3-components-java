package org.pipservices.components.cache;

/**
 * Null cache component that doesn't do caching at all.
 * It's mainly used in testing. However, it can be temporary
 * used to disable cache to troubleshoot problems or study
 * effect of caching on overall system performance. 
 */
public class NullCache implements ICache {
	/**
	 * Creates instance of null cache component.
	 */
	public NullCache() {}

	/**
	 * Retrieves a value from the cache by unique key.
	 * It is recommended to use either string GUIDs like '123456789abc'
	 * or unique natural keys prefixed with the functional group
	 * like 'pip-services-storage:block-123'. 
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache
	 * @return a cached value or <b>null</b> if value wasn't found or timeout expired.
	 */
	public Object retrieve(String correlationId, String key) {
		return null;
	}
	
	/**
	 * Stores value identified by unique key in the cache. 
	 * Stale timeout is configured in the component options. 
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache.
	 * @param value a value to store.
	 * @param timeToLive time for value to live in milliseconds
	 * @return a cached value stored in the cache.
	 */
	public Object store(String correlationId, String key, Object value, long timeToLive) {
		return value;
	}
	
	/**
	 * Removes value stored in the cache.
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache.
	 */
	public void remove(String correlationId, String key) { }
}

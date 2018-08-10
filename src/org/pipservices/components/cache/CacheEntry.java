package org.pipservices.components.cache;

/**
 * Holds cached value for in-memory cache.
 */
public class CacheEntry {
	private long _expiration;
	private String _key;
	private Object _value;

	/**
	 * Creates instance of the cache entry.
	 * @param key the unique key used to identify and locate the value.
	 * @param value the cached value.
	 * @param timeout time to live for the object in milliseconds
	 */
	public CacheEntry(String key, Object value, long timeout) {
		_key = key;
		_value = value;
		_expiration = System.currentTimeMillis() + timeout; 
	}
	
	/**
	 * Gets the unique key to identify and locate the value.
	 * @return the value key.
	 */
	public String getKey() { 
		return _key; 
	}

	/**
	 * Gets the cached value.
	 * @return the currently cached value.
	 */
	public Object getValue() {
		return _value;
	}
	
	/**
	 * Changes the cached value and updates creation time.
	 * @param value the new cached value.
	 * @param timeout time to live for the object in milliseconds
	 */
	public void setValue(Object value, long timeout) {
		_value = value;
		_expiration = System.currentTimeMillis() + timeout; 
	}

	/**
	 * Gets the expiration time in milliseconds
	 * @return system milliseconds when object expires
	 */
	public long getExpiration() {
		return _expiration;
	}
	
	/**
	 * Checks if the object expired
	 * @return <code>true</code> if object expired
	 */
	public boolean isExpired() {
		return _expiration < System.currentTimeMillis();
	}
}

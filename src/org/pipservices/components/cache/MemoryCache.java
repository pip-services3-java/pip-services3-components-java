package org.pipservices.components.cache;

import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.commons.run.*;

/**
 * Cache that stores values in the process memory.
 * 
 * Remember: This implementation is not suitable for synchronization of distributed processes.
 * 
 * ### Configuration parameters ###
 * 
 * options:
 *   timeout:               default caching timeout in milliseconds (default: 1 minute)
 *   max_size:              maximum number of values stored in this cache (default: 1000)        
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * MemoryCache cache = new MemoryCache();
 * 
 * cache.store("123", "key1", "ABC", 0);
 * }
 * </pre>
 * @see ICache
 */
public class MemoryCache implements ICache, IReconfigurable, ICleanable {
	private final static long _defaultTimeout = 60000;
	private final static long _defaultMaxSize = 1000;

	private Object _lock = new Object();
	private Map<String, CacheEntry> _cache = new HashMap<String, CacheEntry>();
	private int _count = 0;
	private long _timeout = _defaultTimeout;
	private long _maxSize = _defaultMaxSize;

	/**
	 * Creates instance of local in-memory cache component
	 */
	public MemoryCache() {
	}

	/**
	 * Configures component by passing configuration parameters.
	 * 
	 * @param config configuration parameters to be set.
	 * @throws ConfigException when component is in illegal state or configuration
	 *                         validation fails.
	 */
	public void configure(ConfigParams config) throws ConfigException {
		_timeout = config.getAsLongWithDefault("timeout", _timeout);
		_maxSize = config.getAsLongWithDefault("max_size", _maxSize);
	}

	/**
	 * Clears component state.
	 */
	private void cleanup() {
		CacheEntry oldest = null;
		_count = 0;

		// Cleanup obsolete entries and find the oldest
		for (Map.Entry<String, CacheEntry> e : _cache.entrySet()) {
			String key = e.getKey();
			CacheEntry entry = e.getValue();
			// Remove obsolete entry
			if (entry.isExpired()) {
				_cache.remove(key);
			}
			// Count the remaining entry
			else {
				_count++;
				if (oldest == null || oldest.getExpiration() > entry.getExpiration())
					oldest = entry;
			}
		}

		// Remove the oldest if cache size exceeded maximum
		if (_count > _maxSize && oldest != null) {
			_cache.remove(oldest.getKey());
			_count--;
		}
	}

	/**
	 * Retrieves cached value from the cache using its key. If value is missing in
	 * the cache or expired it returns null.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a unique value key.
	 * @return a cached value or null if value wasn't found or timeout expired.
	 */
	public Object retrieve(String correlationId, String key) {
		synchronized (_lock) {
			// Get entry from the cache
			CacheEntry entry = _cache.get(key);

			// Cache has nothing
			if (entry == null) {
				return null;
			}

			// Remove entry if expiration set and entry is expired
			if (entry.isExpired()) {
				_cache.remove(key);
				_count--;
				return null;
			}

			// Update access timeout
			return entry.getValue();
		}
	}

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
	public Object store(String correlationId, String key, Object value, long timeout) {
		synchronized (_lock) {
			// Get the entry
			CacheEntry entry = _cache.get(key);
			timeout = timeout > 0 ? timeout : _timeout;

			// Shortcut to remove entry from the cache
			if (value == null) {
				if (entry != null) {
					_cache.remove(key);
					_count--;
				}
				return null;
			}

			// Update the entry
			if (entry != null) {
				entry.setValue(value, timeout);
			}
			// Or create a new entry
			else {
				entry = new CacheEntry(key, value, timeout);
				_cache.put(key, entry);
				_count++;
			}

			// Clean up the cache
			if (_maxSize > 0 && _count > _maxSize)
				cleanup();

			return value;
		}
	}

	/**
	 * Removes a value from the cache by its key.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a unique value key.
	 */
	public void remove(String correlationId, String key) {
		synchronized (_lock) {
			// Get the entry
			CacheEntry entry = _cache.get(key);

			// Remove entry from the cache
			if (entry != null) {
				_cache.remove(key);
				_count--;
			}
		}
	}

	/**
	 * Clears component state.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 */
	public void clear(String correlationId) {
		synchronized (_lock) {
			_cache.clear();
			_count = 0;
		}
	}
}

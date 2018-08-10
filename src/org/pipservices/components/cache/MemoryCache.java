package org.pipservices.components.cache;

import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.commons.run.*;

/**
 * Local in-memory cache that can be used in non-scaled deployments or for testing.
 * 
 * Todo: Track access time for cached entries to optimize cache shrinking logic
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
	public MemoryCache() {}

	/**
	 * Sets component configuration parameters and switches from component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws ConfigException when component is in illegal state 
	 * or configuration validation fails. 
	 */
	public void configure(ConfigParams config) throws ConfigException {
		_timeout = config.getAsLongWithDefault("timeout", _timeout);
		_maxSize = config.getAsLongWithDefault("max_size", _maxSize);
	}
	
	/**
	 * Cleans up cache from obsolete values and shrinks the cache
	 * to fit into allowed max size by dropping values that were not
	 * accessed for a long time
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
	 * Retrieves a value from the cache by unique key.
	 * It is recommended to use either string GUIDs like '123456789abc'
	 * or unique natural keys prefixed with the functional group
	 * like 'pip-services-storage:block-123'. 
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache
	 * @return a cached value or <b>null</b> if value wasn't found or timeout expired.
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
	 * Stores value identified by unique key in the cache. 
	 * Stale timeout is configured in the component options. 
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache.
	 * @param value a value to store.
	 * @param timeout time for value to live in milliseconds
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
	 * Removes value stored in the cache.
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a unique key to locate value in the cache.
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
	
    public void clear(String correlationId) {
    	synchronized (_lock) {
    		_cache.clear();
    		_count = 0;
    	}
    }
}

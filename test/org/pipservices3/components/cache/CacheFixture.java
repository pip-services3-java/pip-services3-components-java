package org.pipservices3.components.cache;

import static org.junit.Assert.*;

public class CacheFixture {
    private ICache _cache;

    public CacheFixture(ICache cache) {
        _cache = cache;
    }

    public void testBasicOperations() {
    	// Set the first value
    	Object value = _cache.store(null, "test", 123, 0);
    	assertEquals(123, value);
    	
    	value = _cache.retrieve(null, "test");
    	assertEquals(123, value);

    	// Set null value
    	value = _cache.store(null, "test", null, 0);
    	assertNull(value);

		value = _cache.retrieve(null, "test");
		assertNull(value);
		
		// Set the second value
    	value = _cache.store(null, "test", "ABC", 0);
    	assertEquals("ABC", value);
    	
    	value = _cache.retrieve(null, "test");
    	assertEquals("ABC", value);

    	// Unset value
    	_cache.remove(null, "test");

		value = _cache.retrieve(null, "test");
		assertNull(value);
    }

    public void testReadAfterTimeout() {
    	// Set value
    	Object value = _cache.store(null, "test", 123, 50);
    	assertEquals(123, value);
    	
    	// Read the value
    	value = _cache.retrieve(null, "test");
    	assertEquals(123, value);
    	
    	// Wait
    	try {
    		Thread.sleep(200);
    	} catch (Exception ex) {
    		// Ignore..
    	}
    	
    	// Read the value again
    	value = _cache.retrieve(null, "test");
    	assertNull(value);
    }
    
}
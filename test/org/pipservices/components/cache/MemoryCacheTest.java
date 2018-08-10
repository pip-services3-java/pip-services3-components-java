package org.pipservices.components.cache;

import org.junit.*;

public class MemoryCacheTest {
	private ICache cache;
	private CacheFixture fixture;
	
	@Before
	public void setUp() throws Exception {
		cache = new MemoryCache();
		fixture = new CacheFixture(cache);
	}
	
	@Test
	public void testBasicOperations() {
		fixture.testBasicOperations();
	}

	@Test
	public void testReadAfterTimeout() {
		fixture.testReadAfterTimeout();
	}
}

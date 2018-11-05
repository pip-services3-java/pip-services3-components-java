package org.pipservices3.components.cache;

import org.pipservices3.components.build.*;
import org.pipservices3.commons.refer.*;

/**
 * Creates {@link ICache} components by their descriptors.
 * 
 * @see Factory
 * @see ICache
 * @see MemoryCache
 * @see NullCache
 */
public class DefaultCacheFactory extends Factory {
	public final static Descriptor Descriptor = new Descriptor("pip-services3-commons", "factory", "cache", "*", "1.0");
	public final static Descriptor MemoryCacheDescriptor = new Descriptor("pip-services3", "cache", "memory", "*",
			"1.0");
	public final static Descriptor NullCacheDescriptor = new Descriptor("pip-services3", "cache", "null", "*", "1.0");

	/**
	 * Create a new instance of the factory.
	 */
	public DefaultCacheFactory() {
		registerAsType(MemoryCacheDescriptor, MemoryCache.class);
		registerAsType(NullCacheDescriptor, NullCache.class);
	}
}

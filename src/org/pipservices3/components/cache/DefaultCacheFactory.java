package org.pipservices3.components.cache;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;

/**
 * Creates {@link ICache} components by their descriptors.
 *
 * @see Factory
 * @see ICache
 * @see MemoryCache
 * @see NullCache
 */
public class DefaultCacheFactory extends Factory {
    public final static Descriptor Descriptor = new Descriptor("pip-services", "factory", "cache", "*", "1.0");
    public final static Descriptor MemoryCacheDescriptor = new Descriptor("pip-services", "cache", "memory", "*",
            "1.0");
    public final static Descriptor NullCacheDescriptor = new Descriptor("pip-services", "cache", "null", "*", "1.0");

    /**
     * Create a new instance of the factory.
     */
    public DefaultCacheFactory() {
        registerAsType(MemoryCacheDescriptor, MemoryCache.class);
        registerAsType(NullCacheDescriptor, NullCache.class);
    }
}

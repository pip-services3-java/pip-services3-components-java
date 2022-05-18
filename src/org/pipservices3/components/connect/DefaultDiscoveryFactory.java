package org.pipservices3.components.connect;

import org.pipservices3.components.build.*;
import org.pipservices3.commons.refer.*;

/**
 * Creates {@link IDiscovery} components by their descriptors.
 * 
 * @see Factory
 * @see IDiscovery
 * @see MemoryDiscovery
 */
public class DefaultDiscoveryFactory extends Factory {
	public static final Descriptor MemoryDiscoveryDescriptor = new Descriptor("pip-services", "discovery", "memory",
			"*", "1.0");

	/**
	 * Create a new instance of the factory.
	 */
	public DefaultDiscoveryFactory() {
		registerAsType(MemoryDiscoveryDescriptor, MemoryDiscovery.class);
	}
}

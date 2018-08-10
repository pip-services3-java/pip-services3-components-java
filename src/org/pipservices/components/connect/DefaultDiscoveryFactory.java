package org.pipservices.components.connect;

import org.pipservices.components.build.*;
import org.pipservices.commons.refer.*;

public class DefaultDiscoveryFactory extends Factory {
	public static final Descriptor Descriptor = new Descriptor("pip-services-commons", "factory", "discovery", "default", "1.0");
	public static final Descriptor MemoryDiscoveryDescriptor = new Descriptor("pip-services", "discovery", "memory", "*", "1.0");
		
	public DefaultDiscoveryFactory() {
		registerAsType(MemoryDiscoveryDescriptor, MemoryDiscovery.class);
	}	
}

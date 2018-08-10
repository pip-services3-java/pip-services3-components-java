package org.pipservices.components.auth;

import org.pipservices.components.build.*;
import org.pipservices.commons.refer.*;

public class DefaultCredentialStoreFactory extends Factory {
	public static final Descriptor Descriptor = new Descriptor("pip-services-commons", "factory", "credential-store", "default", "1.0");
	public static final Descriptor MemoryCredentialStoreDescriptor = new Descriptor("pip-services", "credential-store", "memory", "*", "1.0");
	
	
	public DefaultCredentialStoreFactory() {
		registerAsType(MemoryCredentialStoreDescriptor, MemoryCredentialStore.class);
	}
}

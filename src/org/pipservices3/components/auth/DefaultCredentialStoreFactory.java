package org.pipservices3.components.auth;

import org.pipservices3.components.build.*;
import org.pipservices3.commons.refer.*;

/**
 * Creates ICredentialStore components by their descriptors.
 * 
 * @see IFactory
 * @see ICredentialStore
 * @see MemoryCredentialStore
 */
public class DefaultCredentialStoreFactory extends Factory {
	public static final Descriptor Descriptor = new Descriptor("pip-services3-commons", "factory", "credential-store",
			"default", "1.0");
	public static final Descriptor MemoryCredentialStoreDescriptor = new Descriptor("pip-services3", "credential-store",
			"memory", "*", "1.0");

	/**
	 * Create a new instance of the factory.
	 */
	public DefaultCredentialStoreFactory() {
		super();
		registerAsType(MemoryCredentialStoreDescriptor, MemoryCredentialStore.class);
	}
}

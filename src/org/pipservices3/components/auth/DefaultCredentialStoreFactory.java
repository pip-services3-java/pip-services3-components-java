package org.pipservices3.components.auth;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;
import org.pipservices3.components.build.IFactory;

/**
 * Creates ICredentialStore components by their descriptors.
 *
 * @see IFactory
 * @see ICredentialStore
 * @see MemoryCredentialStore
 */
public class DefaultCredentialStoreFactory extends Factory {
    public static final Descriptor MemoryCredentialStoreDescriptor = new Descriptor("pip-services", "credential-store",
            "memory", "*", "1.0");

    /**
     * Create a new instance of the factory.
     */
    public DefaultCredentialStoreFactory() {
        super();
        registerAsType(MemoryCredentialStoreDescriptor, MemoryCredentialStore.class);
    }
}

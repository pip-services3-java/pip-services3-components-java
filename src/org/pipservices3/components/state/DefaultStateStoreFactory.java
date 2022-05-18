package org.pipservices3.components.state;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;

/**
 * Creates {@link IStateStore} components by their descriptors.
 */
public class DefaultStateStoreFactory extends Factory {

    public static final org.pipservices3.commons.refer.Descriptor Descriptor = new Descriptor("pip-services", "factory", "state-store", "default", "1.0");
    public static final Descriptor NullStateStoreDescriptor = new Descriptor("pip-services", "state-store", "null", "*", "1.0");
    public static final Descriptor MemoryStateStoreDescriptor = new Descriptor("pip-services", "state-store", "memory", "*", "1.0");

    /**
     * Create a new instance of the factory.
     *
     * @see Factory
     * @see IStateStore
     * @see MemoryStateStore
     * @see NullStateStore
     */
    public DefaultStateStoreFactory() {
        this.registerAsType(DefaultStateStoreFactory.MemoryStateStoreDescriptor, MemoryStateStore.class);
        this.registerAsType(DefaultStateStoreFactory.NullStateStoreDescriptor, NullStateStore.class);
    }
}

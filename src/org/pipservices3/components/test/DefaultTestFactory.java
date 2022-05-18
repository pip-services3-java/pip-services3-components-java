package org.pipservices3.components.test;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;

/**
 * Creates test components by their descriptors.
 *
 * @see Factory
 * @see Shutdown
 */
public class DefaultTestFactory extends Factory {
    private static final Descriptor ShutdownDescriptor = new Descriptor("pip-services", "shutdown", "*", "*", "1.0");

    /**
     * Create a new instance of the factory.
     */
    public DefaultTestFactory() {
        this.registerAsType(DefaultTestFactory.ShutdownDescriptor, Shutdown.class);
    }
}

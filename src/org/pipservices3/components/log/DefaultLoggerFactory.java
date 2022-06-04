package org.pipservices3.components.log;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;

/**
 * Creates {@link ILogger} components by their descriptors.
 *
 * @see Factory
 * @see NullLogger
 * @see ConsoleLogger
 * @see CompositeLogger
 */
public class DefaultLoggerFactory extends Factory {
    public final static Descriptor ConsoleLoggerDescriptor = new Descriptor("pip-services", "logger", "console", "*",
            "1.0");
    public final static Descriptor CompositeLoggerDescriptor = new Descriptor("pip-services", "logger", "composite",
            "*", "1.0");
    public final static Descriptor NullLoggerDescriptor = new Descriptor("pip-services", "logger", "null", "*", "1.0");


    /**
     * Create a new instance of the factory.
     */
    public DefaultLoggerFactory() {
        registerAsType(NullLoggerDescriptor, NullLogger.class);
        registerAsType(ConsoleLoggerDescriptor, ConsoleLogger.class);
        registerAsType(CompositeLoggerDescriptor, CompositeLogger.class);
    }
}

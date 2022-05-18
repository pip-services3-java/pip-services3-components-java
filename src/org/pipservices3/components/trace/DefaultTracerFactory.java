package org.pipservices3.components.trace;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;

/**
 * Creates {@link ITracer} components by their descriptors.
 *
 * @see Factory
 * @see NullTracer
 * @see CompositeTracer
 */
public class DefaultTracerFactory extends Factory {
    private static final Descriptor NullTracerDescriptor = new Descriptor("pip-services", "tracer", "null", "*", "1.0");
    private static final Descriptor LogTracerDescriptor = new Descriptor("pip-services", "tracer", "log", "*", "1.0");
    private static final Descriptor CompositeTracerDescriptor = new Descriptor("pip-services", "tracer", "composite", "*", "1.0");

    /**
     * Create a new instance of the factory.
     */
    public DefaultTracerFactory() {
        this.registerAsType(DefaultTracerFactory.NullTracerDescriptor, NullTracer.class);
        this.registerAsType(DefaultTracerFactory.LogTracerDescriptor, LogTracer.class);
        this.registerAsType(DefaultTracerFactory.CompositeTracerDescriptor, CompositeTracer.class);
    }
}

package org.pipservices3.components.config;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;

/**
 * Creates {@link IConfigReader} components by their descriptors.
 *
 * @see Factory
 * @see MemoryConfigReader
 * @see JsonConfigReader
 * @see YamlConfigReader
 */
public class DefaultConfigReaderFactory extends Factory {
    public static final Descriptor MemoryConfigReaderDescriptor = new Descriptor("pip-services", "config-reader",
            "memory", "*", "1.0");
    public static final Descriptor JsonConfigReaderDescriptor = new Descriptor("pip-services", "config-reader", "json",
            "*", "1.0");
    public static final Descriptor YamlConfigReaderDescriptor = new Descriptor("pip-services", "config-reader", "yaml",
            "*", "1.0");

    /**
     * Create a new instance of the factory.
     */
    public DefaultConfigReaderFactory() {
        registerAsType(MemoryConfigReaderDescriptor, MemoryConfigReader.class);
        registerAsType(JsonConfigReaderDescriptor, JsonConfigReader.class);
        registerAsType(YamlConfigReaderDescriptor, YamlConfigReader.class);
    }
}

package org.pipservices3.components.log;

import org.pipservices3.components.build.*;
import org.pipservices3.commons.refer.*;

/**
 * Creates {@link ILogger} components by their descriptors.
 * 
 * @see Factory
 * @see NullLogger
 * @see ConsoleLogger
 * @see CompositeLogger
 */
public class DefaultLoggerFactory extends Factory {
	public final static Descriptor Descriptor = new Descriptor("pip-services3-commons", "factory", "logger", "*", "1.0");
	public final static Descriptor ConsoleLoggerDescriptor = new Descriptor("pip-services3", "logger", "console", "*",
			"1.0");
	public final static Descriptor CompositeLoggerDescriptor = new Descriptor("pip-services3", "logger", "composite",
			"*", "1.0");
	public final static Descriptor NullLoggerDescriptor = new Descriptor("pip-services3", "logger", "null", "*", "1.0");

	public final static Descriptor DiagnosticsLoggerDescriptor = new Descriptor("pip-services3", "logger", "diagnostics",
			"*", "1.0");

	/**
	 * Create a new instance of the factory.
	 */
	public DefaultLoggerFactory() {
		registerAsType(NullLoggerDescriptor, NullLogger.class);
		registerAsType(ConsoleLoggerDescriptor, ConsoleLogger.class);
		registerAsType(CompositeLoggerDescriptor, CompositeLogger.class);

		registerAsType(DiagnosticsLoggerDescriptor, DiagnosticsLogger.class);
	}
}

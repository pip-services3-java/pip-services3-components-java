package org.pipservices3.components.count;

import org.pipservices3.components.build.*;
import org.pipservices3.commons.refer.*;

/**
 * Creates {@link ICounters} components by their descriptors.
 * 
 * @see Factory
 * @see NullCounters
 * @see LogCounters
 * @see CompositeCounters
 */
public class DefaultCountersFactory extends Factory {
	public final static Descriptor Descriptor = new Descriptor("pip-services3-commons", "factory", "counters", "default",
			"1.0");
	public final static Descriptor LogCountersDescriptor = new Descriptor("pip-services3", "counters", "log", "*",
			"1.0");
	public final static Descriptor CompositeCountersDescriptor = new Descriptor("pip-services3", "counters", "composite",
			"*", "1.0");
	public final static Descriptor NullCountersDescriptor = new Descriptor("pip-services3", "counters", "null", "*",
			"1.0");

	/**
	 * Create a new instance of the factory.
	 */
	public DefaultCountersFactory() {
		registerAsType(NullCountersDescriptor, NullCounters.class);
		registerAsType(LogCountersDescriptor, LogCounters.class);
		registerAsType(CompositeCountersDescriptor, CompositeCounters.class);
	}
}

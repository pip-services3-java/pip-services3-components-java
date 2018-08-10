package org.pipservices.components.count;

import java.time.ZonedDateTime;
import java.util.*;

import org.pipservices.commons.refer.*;

public class CompositeCounters implements ICounters, ITimingCallback, IReferenceable {
	private List<ICounters> _counters = new ArrayList<ICounters>();
	
	public CompositeCounters() {}

	public void setReferences(IReferences references) throws ReferenceException {
		List<Object> counters = references.getOptional(new Descriptor(null, "counters", null, null, null)); 
		for (Object counter : counters) {
			if (counter instanceof ICounters && counter != this)
				_counters.add((ICounters)counter);
		}
	}

	public Timing beginTiming(String name) {
		return new Timing(name, this);
	}

	public void endTiming(String name, float elapsed) {
		for (ICounters counter : _counters) {
			if (counter instanceof ITimingCallback)
				((ITimingCallback)counter).endTiming(name, elapsed);
		}
	}

	public void stats(String name, float value) {
		for (ICounters counter : _counters)
			counter.stats(name, value);
	}

	public void last(String name, float value) {
		for (ICounters counter : _counters)
			counter.last(name, value);
	}

	public void timestampNow(String name) {
		timestamp(name, ZonedDateTime.now());
	}

	public void timestamp(String name, ZonedDateTime value) {
		for (ICounters counter : _counters)
			counter.timestamp(name, value);
	}

	public void incrementOne(String name) {
		increment(name, 1);
	}

	public void increment(String name, int value) {
		for (ICounters counter : _counters)
			counter.increment(name, value);
	}
	
}

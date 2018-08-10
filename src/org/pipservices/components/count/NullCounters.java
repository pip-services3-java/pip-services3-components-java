package org.pipservices.components.count;

import java.time.*;

public class NullCounters implements ICounters {

	public NullCounters() {}
		
	public Timing beginTiming(String name) { return new Timing(); }
	public void stats(String name, float value) {}
	public void last(String name, float value) {}
	public void timestampNow(String name) {}
	public void timestamp(String name, ZonedDateTime value) {}
	public void incrementOne(String name) {}
	public void increment(String name, int value) {}
}

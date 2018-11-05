package org.pipservices3.components.count;

import java.time.ZonedDateTime;
import java.util.*;

import org.pipservices3.commons.refer.*;

/**
 * Aggregates all counters from component references under a single component.
 * <p>
 * It allows to capture metrics and conveniently send them to multiple destinations. 
 * <p>
 * ### References ###
 * <ul>
 * <li>*:counters:*:*:1.0         (optional) {@link ICounters} components to pass collected measurements
 * </ul>
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * class MyComponent implements IReferenceable {
 *     CompositeCounters _counters = new CompositeCounters();
 * 
 *     public void setReferences(IReferences references) {
 *         this._counters.setReferences(references);
 *         ...
 *     }
 * 
 *     public void myMethod() {
 *         this._counters.increment("mycomponent.mymethod.calls");
 *         Timing timing = this._counters.beginTiming("mycomponent.mymethod.exec_time");
 *         try {
 *            ...
 *         } finally {
 *             timing.endTiming();
 *         }
 *     }
 * }
 * }
 * </pre>
 * @see ICounters
 */
public class CompositeCounters implements ICounters, ITimingCallback, IReferenceable {
	private List<ICounters> _counters = new ArrayList<ICounters>();

	/**
	 * Creates a new instance of the counters.
	 */
	public CompositeCounters() {
	}

	/**
	 * Sets references to dependent components.
	 * 
	 * @param references references to locate the component dependencies.
	 * @throws ReferenceException when no references found.
	 */
	public void setReferences(IReferences references) throws ReferenceException {
		List<Object> counters = references.getOptional(new Descriptor(null, "counters", null, null, null));
		for (Object counter : counters) {
			if (counter instanceof ICounters && counter != this)
				_counters.add((ICounters) counter);
		}
	}

	/**
	 * Begins measurement of execution time interval. It returns Timing object which
	 * has to be called at {@link Timing#endTiming()} to end the measurement and
	 * update the counter.
	 * 
	 * @param name a counter name of Interval type.
	 * @return a Timing callback object to end timing.
	 */
	public Timing beginTiming(String name) {
		return new Timing(name, this);
	}

	/**
	 * Ends measurement of execution elapsed time and updates specified counter.
	 * 
	 * @param name    a counter name
	 * @param elapsed execution elapsed time in milliseconds to update the counter.
	 * 
	 * @see Timing#endTiming()
	 */
	public void endTiming(String name, float elapsed) {
		for (ICounters counter : _counters) {
			if (counter instanceof ITimingCallback)
				((ITimingCallback) counter).endTiming(name, elapsed);
		}
	}

	/**
	 * Calculates min/average/max statistics based on the current and previous
	 * values.
	 * 
	 * @param name  a counter name of Statistics type
	 * @param value a value to update statistics
	 */
	public void stats(String name, float value) {
		for (ICounters counter : _counters)
			counter.stats(name, value);
	}

	/**
	 * Records the last calculated measurement value.
	 * 
	 * Usually this method is used by metrics calculated externally.
	 * 
	 * @param name  a counter name of Last type.
	 * @param value a last value to record.
	 */
	public void last(String name, float value) {
		for (ICounters counter : _counters)
			counter.last(name, value);
	}

	/**
	 * Records the current time as a timestamp.
	 * 
	 * @param name a counter name of Timestamp type.
	 */
	public void timestampNow(String name) {
		timestamp(name, ZonedDateTime.now());
	}

	/**
	 * Records the given timestamp.
	 * 
	 * @param name  a counter name of Timestamp type.
	 * @param value a timestamp to record.
	 */
	public void timestamp(String name, ZonedDateTime value) {
		for (ICounters counter : _counters)
			counter.timestamp(name, value);
	}

	/**
	 * Increments counter by 1.
	 * 
	 * @param name a counter name of Increment type.
	 */
	public void incrementOne(String name) {
		increment(name, 1);
	}

	/**
	 * Increments counter by given value.
	 * 
	 * @param name  a counter name of Increment type.
	 * @param value a value to add to the counter.
	 */
	public void increment(String name, int value) {
		for (ICounters counter : _counters)
			counter.increment(name, value);
	}

}

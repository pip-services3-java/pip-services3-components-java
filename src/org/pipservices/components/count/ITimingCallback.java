package org.pipservices.components.count;

import org.pipservices.components.log.*;

/**
 * Interface for a callback to end measurement of execution elapsed time.
 * 
 * @see Timing
 */
public interface ITimingCallback {
	/**
	 * Ends measurement of execution elapsed time and updates specified counter.
	 * 
	 * @param name    a counter name
	 * @param elapsed execution elapsed time in milliseconds to update the counter.
	 * 
	 * @see Timing#endTiming()
	 */
	void endTiming(String name, float elapsed);
}

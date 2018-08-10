package org.pipservices.components.count;

/**
 * Interface for Timing callbacks to record captured elapsed time
 */
public interface ITimingCallback {
	/**
	 * Recording calculated elapsed time 
	 * @param name the name of the counter
	 * @param elapsed time in milliseconds
	 */
	void endTiming(String name, float elapsed);
}

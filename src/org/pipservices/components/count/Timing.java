package org.pipservices.components.count;

/**
 * Provides callback to end measuring execution time interface and update interval counter.
 */
public class Timing {
	private long _start;
	private ITimingCallback _callback;
	private String _counter;
	
	/**
	 * Creates instance of timing object that doesn't record anything
	 */
	public Timing() {}
	
	/**
	 * Creates instance of timing object that calculates elapsed time
	 * and stores it to specified performance counters component under specified name.
	 * @param counter a name of the counter to record elapsed time interval.
	 * @param callback a performance counters component to store calculated value.
	 */
	public Timing(String counter, ITimingCallback callback) {
		_counter = counter;
		_callback = callback;
		_start = System.currentTimeMillis();
	}
	
	/**
	 * Completes measuring time interval and updates counter.
	 */
	public void endTiming() {
		if (_callback != null) {
			float elapsed = System.currentTimeMillis() - _start;
			_callback.endTiming(_counter, elapsed);
		}
	}
}

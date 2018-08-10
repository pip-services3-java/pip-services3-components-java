package org.pipservices.components.count;

import java.util.*;

import org.pipservices.commons.convert.*;
import org.pipservices.components.log.*;
import org.pipservices.commons.refer.*;

/**
 * Performance counters component that periodically dumps counters
 * to log as message: 'Counter <name> {"type": <type>, "last": <last>, ...}
 */
public class LogCounters extends CachedCounters implements IReferenceable {
	private CompositeLogger _logger = new CompositeLogger();
	
	/**
	 * Creates instance of log counters component.
	 */
	public LogCounters() {}
	
	public void setReferences(IReferences references) throws ReferenceException {
		_logger.setReferences(references);
	}
	
	/**
	 * Formats counter string representation.
	 * @param counter a counter object to generate a string for.
	 * @return a formatted string representation of the counter.
	 */
    private String counterToString(Counter counter) {
        String result = "Counter " + counter.getName() + " { ";
        result += "\"type\": " + counter.getType();
        if (counter.getLast() != null)
            result += ", \"last\": " + StringConverter.toString(counter.getLast());
        if (counter.getCount() != null)
            result += ", \"count\": " + StringConverter.toString(counter.getCount());
        if (counter.getMin() != null)
            result += ", \"min\": " + StringConverter.toString(counter.getMin());
        if (counter.getMax() != null)
            result += ", \"max\": " + StringConverter.toString(counter.getMax());
        if (counter.getAverage() != null)
            result += ", \"avg\": " + StringConverter.toString(counter.getAverage());
        if (counter.getTime() != null)
            result += ", \"time\": " + StringConverter.toString(counter.getTime());
        result += " }";
        return result;
    }

    /**
     * Outputs a list of counter values to log.
     * @param counter a list of counters to be dump to log.
     */
    @Override
    protected void save(List<Counter> counters) {
    	if (_logger == null) return;
        if (counters.size() == 0) return;

        Collections.sort(
    		counters,
    		(c1, c2) -> c1.getName().compareTo(c2.getName())
		);

        for (Counter counter : counters) {
            _logger.info("counters", counterToString(counter));
        }
    }
}

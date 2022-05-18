package org.pipservices3.components.count;

import java.util.*;

import org.pipservices3.commons.convert.*;
import org.pipservices3.components.log.*;
import org.pipservices3.commons.refer.*;

/**
 * Performance counters that periodically dumps counters measurements to logger.
 * <p>
 * ### Configuration parameters ###
 * <ul>
 * <li>options:
 * <ul>
 *   <li>interval:        interval in milliseconds to save current counters measurements (default: 5 mins)
 *   <li>reset_timeout:   timeout in milliseconds to reset the counters. 0 disables the reset (default: 0)
 * </ul>
 * </ul>
 * <p>
 * ### References ###
 * <ul>
 * <li>*:logger:*:*:1.0           {@link org.pipservices3.components.log.ILogger} components to dump the captured counters
 * <li>*:context-info:*:*:1.0     (optional) {@link org.pipservices3.components.info.ContextInfo} to detect the context id and specify counters source
 * </ul>
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * LogCounters counters = new LogCounters();
 * counters.setReferences(References.fromTuples(
 *     new Descriptor("pip-services3", "logger", "console", "default", "1.0"), new ConsoleLogger()
 * ));
 *
 * counters.increment("mycomponent.mymethod.calls");
 * CounterTiming timing = counters.beginTiming("mycomponent.mymethod.exec_time");
 * try {
 *     ...
 * } finally {
 *     timing.endTiming();
 * }
 *
 * counters.dump();
 * }
 * </pre>
 *
 * @see Counter
 * @see CachedCounters
 * @see CompositeLogger
 */
public class LogCounters extends CachedCounters implements IReferenceable {
    private final CompositeLogger _logger = new CompositeLogger();

    /**
     * Creates a new instance of the counters.
     */
    public LogCounters() {
    }

    /**
     * Sets references to dependent components.
     *
     * @param references references to locate the component dependencies.
     */
    public void setReferences(IReferences references) throws ReferenceException {
        _logger.setReferences(references);
    }

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
     * Saves the current counters measurements.
     *
     * @param counters current counters measurements to be saves.
     */
    @Override
    protected void save(List<Counter> counters) {
        if (counters.size() == 0)
            return;

        counters.sort(Comparator.comparing(Counter::getName));

        for (Counter counter : counters) {
            _logger.info("counters", counterToString(counter));
        }
    }
}

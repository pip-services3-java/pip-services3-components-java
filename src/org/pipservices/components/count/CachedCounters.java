package org.pipservices.components.count;

import java.time.*;
import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;

public abstract class CachedCounters implements ICounters, IReconfigurable, ITimingCallback {
	private final static long _defaultInterval = 300000;
	
    private Map<String, Counter> _cache = new HashMap<String, Counter>();
    private boolean _updated = false;
    private long _lastDumpTime = System.currentTimeMillis();
    protected long _lastResetTime = System.currentTimeMillis();
    private long _interval = _defaultInterval;
    private Object _lock = new Object();
    protected long _resetTimeout = 0;
    
    protected CachedCounters() { }

    protected abstract void save(List<Counter> counters) throws InvocationException;

    public void configure(ConfigParams config) {
    	_interval = config.getAsLongWithDefault("interval", _defaultInterval);
    	_resetTimeout = config.getAsLongWithDefault("reset_timeout", _resetTimeout);
    }
    
    public void clear(String name) {
    	synchronized(_lock) {
    		_cache.remove(name);
    	}
    }

    public void clearAll() {
    	synchronized(_lock) {
	        _cache.clear();
	        _updated = false;
    	}
    }

    public void dump() throws InvocationException {
        if (_updated) {
            List<Counter> counters = this.getAll();
            save(counters);
            
            synchronized (_lock) {
            	_updated = false;
            	_lastDumpTime = System.currentTimeMillis();
            }
        }
    }

    protected void update() {
    	_updated = true;
    	if (System.currentTimeMillis() > _lastDumpTime + _interval) {
    		try {
    			dump();
    		}
    		catch (InvocationException ex) {
    			// Todo: decide what to do
    		}
    	}
    }
    
    private void ResetIfNeeded()
    {
        if (_resetTimeout == 0) return;

        if ( System.currentTimeMillis() - _lastResetTime > _resetTimeout)
        {
            _cache.clear();
            _updated = false;                
            _lastResetTime = System.currentTimeMillis();
        }
    }
    
    public List<Counter> getAll() {
    	synchronized (_lock) {
    		ResetIfNeeded();
    		return new ArrayList<Counter>(_cache.values());
    	}
    }

    public Counter get(String name, int type) {
        if (name == null || name.length() == 0)
            throw new NullPointerException("Counter name was not set");

        synchronized (_lock) {
        	ResetIfNeeded();
	        Counter counter = _cache.get(name);      
	        if (counter == null || counter.getType() != type) {
	            counter = new Counter(name, type);
	            _cache.put(name, counter);
	        }
	
	        return counter;
        }
    }

    private void calculateStats(Counter counter, float value) {
        if (counter == null)
        	throw new NullPointerException("Missing counter");

        counter.setLast(value);
        counter.setCount(counter.getCount() != null ? counter.getCount() + 1 : 1);
        counter.setMax(counter.getMax() != null ? Math.max(counter.getMax(), value) : value);
        counter.setMin(counter.getMin() != null ? Math.min(counter.getMin(), value) : value);
        counter.setAverage(counter.getAverage() != null && counter.getCount() > 1
            ? ((counter.getAverage() * (counter.getCount() - 1)) + value) / counter.getCount() : value);
    }
    
	/**
	 * Starts measurement of execution time interval.
	 * The method returns ITiming object that provides endTiming()
	 * method that shall be called when execution is completed
	 * to calculate elapsed time and update the counter.
	 * @param name the name of interval counter.
	 * @return callback interface with endTiming() method 
	 * that shall be called at the end of execution.
	 */
    public Timing beginTiming(String name) {
        return new Timing(name, this);
    }

    public void endTiming(String name, float elapsed) {
    	Counter counter = get(name, CounterType.Interval);
    	calculateStats(counter, elapsed);
    	update();
    }

	/**
	 * Calculates rolling statistics: minimum, maximum, average
	 * and updates Statistics counter.
	 * This counter can be used to measure various non-functional
	 * characteristics, such as amount stored or transmitted data,
	 * customer feedback, etc. 
	 * @param name the name of statistics counter.
	 * @param value the value to add to statistics calculations.
	 */
    public void stats(String name, float value) {
        Counter counter = get(name, CounterType.Statistics);
        calculateStats(counter, value);
    	update();
    }

	/**
	 * Records the last reported value. 
	 * This counter can be used to store performance values reported
	 * by clients or current numeric characteristics such as number
	 * of values stored in cache.
	 * @param name the name of last value counter
	 * @param value the value to be stored as the last one
	 */
    public void last(String name, float value) {
        Counter counter = get(name, CounterType.LastValue);
        counter.setLast(value);
    	update();
    }

	/**
	 * Records the current time.
	 * This counter can be used to track timing of key
	 * business transactions.
	 * @param name the name of timing counter
	 */
	public void timestampNow(String name) {
		timestamp(name, ZonedDateTime.now());
	}

	/**
	 * Records specified time.
	 * This counter can be used to tack timing of key
	 * business transactions as reported by clients.
	 * @param name the name of timing counter.
	 * @param value the reported timing to be recorded.
	 */
    public void timestamp(String name, ZonedDateTime value) {
        Counter counter = get(name, CounterType.Timestamp);
        counter.setTime(value != null ? value : ZonedDateTime.now(ZoneId.of("Z")));
    	update();
    }

	/**
	 * Increments counter by value of 1.
	 * This counter is often used to calculate
	 * number of client calls or performed transactions.
	 * @param name the name of counter counter.
	 */
	public void incrementOne(String name) {
    	increment(name, 1);
    }

	/**
	 * Increments counter by specified value.
	 * This counter can be used to track various
	 * numeric characteristics
	 * @param name the name of the increment value.
	 * @param value number to increase the counter.
	 */
    public void increment(String name, int value) {
        Counter counter = get(name, CounterType.Increment);
        counter.setCount(counter.getCount() != null
            ? counter.getCount() + value : value);
    	update();
    }
}

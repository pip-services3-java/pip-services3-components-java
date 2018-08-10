package org.pipservices.components.log;

import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;

public abstract class CachedLogger extends Logger implements IReconfigurable {
	private final static long _defaultInterval = 60000;
	
    protected List<LogMessage> _cache = new ArrayList<LogMessage>();
    protected boolean _updated = false;
    protected long _lastDumpTime = System.currentTimeMillis();
    protected long _interval = _defaultInterval;
    protected Object _lock = new Object();
	
	@Override
	protected void write(LogLevel level, String correlationId, Exception ex, String message) {
		ErrorDescription error = ex != null ? ErrorDescriptionFactory.create(ex, correlationId) : null;
		String source = getComputerName(); // Todo: add jar/exe name
		LogMessage logMessage = new LogMessage(level, source, correlationId, error, message);
		
		synchronized (_lock) {
			_cache.add(logMessage);
		}
		
		update();
	}

	private String getComputerName() {
	    Map<String, String> env = System.getenv();
	    if (env.containsKey("COMPUTERNAME"))
	        return env.get("COMPUTERNAME");
	    else if (env.containsKey("HOSTNAME"))
	        return env.get("HOSTNAME");
	    else
	        return "Unknown Computer";
	}	

    protected abstract void save(List<LogMessage> messages) throws InvocationException;

    public void configure(ConfigParams config) {
    	_interval = config.getAsLongWithDefault("interval", _interval);
    }
    
    public void clear() {
    	synchronized(_lock) {
	        _cache.clear();
	        _updated = false;
    	}
    }

    public void dump() throws InvocationException {
        if (_updated) {            
            synchronized (_lock) {
            	if (!_updated) return;
            	
                List<LogMessage> messages = _cache;
                _cache = new ArrayList<LogMessage>();
                
                save(messages);

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
}

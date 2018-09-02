package org.pipservices.components.log;

import java.util.*;

import org.pipservices.commons.refer.*;

public class CompositeLogger extends Logger {	
	private List<ILogger> _loggers = new ArrayList<ILogger>();
	
	public CompositeLogger() {}

	public CompositeLogger(IReferences references) throws ReferenceException {
		setReferences(references);
	}
	
	@Override
	public void setReferences(IReferences references) {
		
		List<Object> loggers = references.getOptional(new Descriptor(null, "logger", null, null, null)); 
		for (Object logger : loggers) {
			if (logger instanceof ILogger && logger != this)
				_loggers.add((ILogger)logger);
		}
	}

	@Override
	protected void write(LogLevel level, String correlationId, Exception error, String message) {
		for (ILogger logger : _loggers)
			logger.log(level, correlationId, error, message);
	}
	
}

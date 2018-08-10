package org.pipservices.components.log;

import org.pipservices.commons.config.*;
import org.pipservices.components.info.ContextInfo;
import org.pipservices.commons.refer.*;

public abstract class Logger implements ILogger, IReconfigurable, IReferenceable {
	private LogLevel _level = LogLevel.Info;
	protected String _source = null;
	
	protected Logger() {}

    public void configure(ConfigParams config) {
        _level = LogLevelConverter.toLogLevel(
    		config.getAsObject("level")
		);
    }
    
    public void setReferences(IReferences references)
    {
        Object contextInfo = references.getOneOptional(
                new Descriptor("pip-services", "context-info", "*", "*", "1.0"));
        if (contextInfo instanceof ContextInfo && contextInfo != null && _source == null)
                _source = ((ContextInfo)contextInfo).getName();
    }
    
    protected String composeError(Exception error)
    {
    	StringBuilder builder = new StringBuilder();

        while (error != null)
        {
            if (builder.length() > 0)
                builder.append(" Caused by error: ");

            builder.append(error.getMessage())
                .append(" StackTrace: ")
                .append(error.getStackTrace());

            try {
				error = error.getClass().newInstance();///// ????
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			} 
        }

        return builder.toString();
    }

	public LogLevel getLevel() { return _level; }
	public void setLevel(LogLevel value) { _level = value; }

	protected abstract void write(LogLevel level, String correlationId, Exception error, String message);

	protected void formatAndWrite(LogLevel level, String correlationId, Exception error, String message, Object[] args) {
        message = message != null ? message : "";
        if (args != null && args.length > 0)
        	message = String.format(message, args);
        
        write(level, correlationId, error, message);
	}
	
    public void log(LogLevel level, String correlationId, Exception error, String message, Object... args) {
    	formatAndWrite(level, correlationId, error, message, args);
    }

    public void fatal(String correlationId, String message, Object... args) {
    	formatAndWrite(LogLevel.Fatal, correlationId, null, message, args);
    }
    
    public void fatal(String correlationId, Exception error) {
    	formatAndWrite(LogLevel.Fatal, correlationId, error, null, null);
    }
    
    public void fatal(String correlationId, Exception error, String message, Object... args) {
    	formatAndWrite(LogLevel.Fatal, correlationId, error, message, args);
    }

    public void error(String correlationId, String message, Object... args) {
    	formatAndWrite(LogLevel.Error, correlationId, null, message, args);
    }
    
    public void error(String correlationId, Exception error) {
    	formatAndWrite(LogLevel.Error, correlationId, error, null, null);
    }
    
    public void error(String correlationId, Exception error, String message, Object... args) {
    	formatAndWrite(LogLevel.Error, correlationId, error, message, args);
    }

    public void warn(String correlationId, String message, Object... args) {
    	formatAndWrite(LogLevel.Warn, correlationId, null, message, args);
    }
    
    public void info(String correlationId, String message, Object... args) {
    	formatAndWrite(LogLevel.Info, correlationId, null, message, args);
    }
    
    public void debug(String correlationId, String message, Object... args) {
    	formatAndWrite(LogLevel.Debug, correlationId, null, message, args);
    }
    
    public void trace(String correlationId, String message, Object... args) {
    	formatAndWrite(LogLevel.Trace, correlationId, null, message, args);
    }
}

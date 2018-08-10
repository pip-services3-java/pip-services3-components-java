package org.pipservices.components.log;

public class NullLogger implements ILogger {
	
	public NullLogger() {}
	
	public LogLevel getLevel() { return LogLevel.None; }
	public void setLevel(LogLevel value) { }

	public void log(LogLevel level, String correlationId, Exception error, String message, Object... args) { }

	public void fatal(String correlationId, String message, Object... args) { }
	public void fatal(String correlationId, Exception error) { }
	public void fatal(String correlationId, Exception error, String message, Object... args) { }

	public void error(String correlationId, String message, Object... args) { }
	public void error(String correlationId, Exception error) { }
	public void error(String correlationId, Exception error, String message, Object... args) { }

	public void warn(String correlationId, String message, Object... args) { }

	public void info(String correlationId, String message, Object... args) { }

	public void debug(String correlationId, String message, Object... args) { }

	public void trace(String correlationId, String message, Object... args) { }
}

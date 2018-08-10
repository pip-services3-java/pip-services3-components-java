package org.pipservices.components.log;

/**
 * Captures and writes log messages
 */
public interface ILogger {
	LogLevel getLevel();
	void setLevel(LogLevel value);

    void log(LogLevel level, String correlationId, Exception error, String message, Object... args);

    void fatal(String correlationId, String message, Object... args);
    void fatal(String correlationId, Exception error);
    void fatal(String correlationId, Exception error, String message, Object... args);

    void error(String correlationId, String message, Object... args);
    void error(String correlationId, Exception error);
    void error(String correlationId, Exception error, String message, Object... args);

    void warn(String correlationId, String message, Object... args);
    void info(String correlationId, String message, Object... args);
    void debug(String correlationId, String message, Object... args);
    void trace(String correlationId, String message, Object... args);
}

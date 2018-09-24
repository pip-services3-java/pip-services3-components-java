package org.pipservices.components.log;

/**
 * Interface for logger components that capture execution log messages.
 */
public interface ILogger {
	/**
	 * Gets the maximum log level. Messages with higher log level are filtered out.
	 * 
	 * @return the maximum log level.
	 */
	LogLevel getLevel();

	/**
	 * Set the maximum log level.
	 * 
	 * @param value a new maximum log level.
	 */
	void setLevel(LogLevel value);

	/**
	 * Logs a message at specified log level.
	 * 
	 * @param level         a log level.
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param error         an error object associated with this message.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void log(LogLevel level, String correlationId, Exception error, String message, Object... args);

	/**
	 * Logs fatal (unrecoverable) message that caused the process to crash.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void fatal(String correlationId, String message, Object... args);

	/**
	 * Logs fatal (unrecoverable) message that caused the process to crash.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param error         an error object associated with this message.
	 */
	void fatal(String correlationId, Exception error);

	/**
	 * Logs fatal (unrecoverable) message that caused the process to crash.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param error         an error object associated with this message.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void fatal(String correlationId, Exception error, String message, Object... args);

	/**
	 * Logs recoverable application error.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void error(String correlationId, String message, Object... args);

	/**
	 * Logs recoverable application error.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param error         an error object associated with this message.
	 */
	void error(String correlationId, Exception error);

	/**
	 * Logs recoverable application error.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param error         an error object associated with this message.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void error(String correlationId, Exception error, String message, Object... args);

	/**
	 * Logs a warning that may or may not have a negative impact.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void warn(String correlationId, String message, Object... args);

	/**
	 * Logs an important information message
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void info(String correlationId, String message, Object... args);

	/**
	 * Logs a high-level debug information for troubleshooting.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void debug(String correlationId, String message, Object... args);

	/**
	 * Logs a low-level debug information for troubleshooting.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param message       a human-readable message to log.
	 * @param args          arguments to parameterize the message.
	 */
	void trace(String correlationId, String message, Object... args);
}

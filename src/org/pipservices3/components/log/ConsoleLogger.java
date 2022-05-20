package org.pipservices3.components.log;

import java.time.*;
import java.time.format.*;
import java.util.Arrays;

/**
 * Logger that writes log messages to console.
 * <p>
 * Errors are written to standard err stream
 * and all other messages to standard out stream.
 * <p>
 * ### Configuration parameters ###
 * <ul>
 * <li>level:             maximum log level to capture
 * <li>source:            source (context) name
 * </ul>
 * <p>
 * ### References ###
 * <ul>
 * <li>*:context-info:*:*:1.0     (optional) {@link org.pipservices3.components.info.ContextInfo} to detect the context id and specify counters source
 * </ul>
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ConsoleLogger logger = new ConsoleLogger();
 * logger.setLevel(LogLevel.debug);
 * 
 * logger.error("123", ex, "Error occured: %s", ex.message);
 * logger.debug("123", "Everything is OK.");
 * }
 * </pre>
 * @see Logger
 */
public class ConsoleLogger extends Logger {

	/**
	 * Creates a new instance of the logger.
	 */
	public ConsoleLogger() {
		super();
	}

	/**
	 * Writes a log message to the logger destination.
	 * 
	 * @param level         a log level.
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param error         an error object associated with this message.
	 * @param message       a human-readable message to log.
	 */
	@Override
	protected void write(LogLevel level, String correlationId, Exception error, String message) {
		if (LogLevelConverter.toInteger(this.getLevel()) < LogLevelConverter.toInteger(level))
			return;

		StringBuilder build = new StringBuilder();

		build.append('[');
		build.append(correlationId != null ? correlationId : "---");
		build.append(':');
		build.append(LogLevelConverter.toString(level));
		build.append(':');
		build.append(ZonedDateTime.now(ZoneId.of("Z")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		build.append("] ");

		build.append(message);

		if (error != null) {
			if (message.length() == 0)
				build.append("Error: ");
			else
				build.append(": ");

			build.append(composeError(error));
		}

		String output = build.toString();

		if (level == LogLevel.Fatal || level == LogLevel.Error || level == LogLevel.Warn)
			System.err.println(output);
		else
			System.out.println(output);
	}
}

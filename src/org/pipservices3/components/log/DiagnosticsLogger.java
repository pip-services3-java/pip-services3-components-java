package org.pipservices3.components.log;

import java.text.DateFormat;

import org.pipservices3.commons.convert.StringConverter;

public class DiagnosticsLogger extends Logger {

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
		if (this.getLevel().equals(level))
			return;

		StringBuilder build = new StringBuilder();
		build.append('[');
		build.append(correlationId != null ? correlationId : "---");
		build.append(':');
		build.append(level.toString());
		build.append(':');
		build.append(StringConverter
				.toString(DateFormat.getDateInstance(DateFormat.SHORT).format(System.currentTimeMillis())));
		build.append("] ");

		build.append(message);

		if (error != null) {
			if (message.length() == 0)
				build.append("Error: ");
			else
				build.append(": ");

			build.append(error.toString());
		}

		String output = build.toString();

		if (level == LogLevel.Fatal || level == LogLevel.Error || level == LogLevel.Warn)
			System.err.println(output);
		else
			System.out.println(output);

	}

}

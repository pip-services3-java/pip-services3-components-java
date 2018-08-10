package org.pipservices.components.log;

import java.time.*;
import java.time.format.*;

public class ConsoleLogger extends Logger {

	public ConsoleLogger() {}

    protected String composeError(Exception error) {
        StringBuilder builder = new StringBuilder();

        Throwable t = error;
        while (t != null) {
            if (builder.length() > 0)
                builder.append(" Caused by error: ");

            builder.append(t.getMessage())
            	.append(" StackTrace: ")
            	.append(t.getStackTrace());

            t = t.getCause();
        }

        return builder.toString();
    }
    
    @Override
    protected void write(LogLevel level, String correlationId, Exception error, String message) {
        if (LogLevelConverter.toInteger(this.getLevel()) < LogLevelConverter.toInteger(level)) return;

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

        if (level == LogLevel.Fatal 
    		|| level == LogLevel.Error 
    		|| level == LogLevel.Warn)
            System.err.println(output);
        else
            System.out.println(output);
    }                
}

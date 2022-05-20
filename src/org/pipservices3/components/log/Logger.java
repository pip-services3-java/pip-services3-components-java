package org.pipservices3.components.log;

import org.pipservices3.commons.config.*;
import org.pipservices3.components.info.ContextInfo;
import org.pipservices3.commons.refer.*;

import java.util.Arrays;

/**
 * Abstract logger that captures and formats log messages.
 * Child classes take the captured messages and write them to their specific destinations.
 * <p>
 * ### Configuration parameters ###
 * <p>
 * Parameters to pass to the configure() method for component configuration:
 * <ul>
 * <li>level:             maximum log level to capture
 * <li>source:            source (context) name
 * </ul>
 * <p>
 * ### References ###
 * <ul>
 * <li>*:context-info:*:*:1.0     (optional) {@link ContextInfo} to detect the context id and specify counters source
 * </ul>
 *
 * @see ILogger
 */
public abstract class Logger implements ILogger, IReconfigurable, IReferenceable {
    private LogLevel _level = LogLevel.Info;
    protected String _source = null;

    /**
     * Creates a new instance of the logger.
     */
    protected Logger() {
    }

    /**
     * Configures component by passing configuration parameters.
     *
     * @param config configuration parameters to be set.
     */
    public void configure(ConfigParams config) {
        this._level = LogLevelConverter.toLogLevel(
                config.getAsObject("level"),
                this._level
        );

        this._source = config.getAsStringWithDefault("source", this._source);
    }

    /**
     * Sets references to dependent components.
     *
     * @param references references to locate the component dependencies.
     */
    public void setReferences(IReferences references) {
        Object contextInfo = references.getOneOptional(new Descriptor("pip-services", "context-info", "*", "*", "1.0"));
        if (contextInfo instanceof ContextInfo && _source == null)
            _source = ((ContextInfo) contextInfo).getName();
    }

    /**
     * Composes an human-readable error description
     *
     * @param error an error to format.
     * @return a human-reable error description.
     */
    protected String composeError(Exception error) {
        StringBuilder builder = new StringBuilder();

        Throwable t = error;
        while (t != null) {
            if (builder.length() > 0)
                builder.append(" Caused by error: ");

            builder.append(t.getMessage()).append(" StackTrace: ").append(Arrays.toString(t.getStackTrace()));

            t = t.getCause();
        }

        return builder.toString();
    }

//	/**
//	 * Composes an human-readable error description
//	 *
//	 * @param error an error to format.
//	 * @return a human-reable error description.
//	 */
//	protected String composeError(Exception error) {
//		StringBuilder builder = new StringBuilder();
//
//		while (error != null) {
//			if (builder.length() > 0)
//				builder.append(" Caused by error: ");
//
//			builder.append(error.getMessage()).append(" StackTrace: ").append(Arrays.toString(error.getStackTrace()));
//
//			try {
//				error = error.getClass().newInstance();///// ????
//			} catch (InstantiationException | IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return builder.toString();
//	}

    /**
     * Gets the source (context) name.
     *
     * @return the source (context) name.
     */
    public String getSource() {
        return this._source;
    }

    /**
     * Sets the source (context) name.
     *
     * @param value a new source (context) name.
     */
    public void setSource(String value) {
        _source = value;
    }

    /**
     * Gets the maximum log level. Messages with higher log level are filtered out.
     *
     * @return the maximum log level.
     */
    public LogLevel getLevel() {
        return _level;
    }

    /**
     * Set the maximum log level.
     *
     * @param value a new maximum log level.
     */
    public void setLevel(LogLevel value) {
        _level = value;
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
    protected abstract void write(LogLevel level, String correlationId, Exception error, String message);

    /**
     * Formats the log message and writes it to the logger destination.
     *
     * @param level         a log level.
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param error         an error object associated with this message.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    protected void formatAndWrite(LogLevel level, String correlationId, Exception error, String message,
                                  Object[] args) {
        message = message != null ? message : "";
        if (args != null && args.length > 0)
            message = String.format(message, args);

        write(level, correlationId, error, message);
    }

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
    public void log(LogLevel level, String correlationId, Exception error, String message, Object... args) {
        formatAndWrite(level, correlationId, error, message, args);
    }

    /**
     * Logs fatal (unrecoverable) message that caused the process to crash.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    public void fatal(String correlationId, String message, Object... args) {
        formatAndWrite(LogLevel.Fatal, correlationId, null, message, args);
    }

    /**
     * Logs fatal (unrecoverable) message that caused the process to crash.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param error         an error object associated with this message.
     */
    public void fatal(String correlationId, Exception error) {
        formatAndWrite(LogLevel.Fatal, correlationId, error, null, null);
    }

    /**
     * Logs fatal (unrecoverable) message that caused the process to crash.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param error         an error object associated with this message.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    public void fatal(String correlationId, Exception error, String message, Object... args) {
        formatAndWrite(LogLevel.Fatal, correlationId, error, message, args);
    }

    /**
     * Logs recoverable application error.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    public void error(String correlationId, String message, Object... args) {
        formatAndWrite(LogLevel.Error, correlationId, null, message, args);
    }

    /**
     * Logs recoverable application error.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param error         an error object associated with this message.
     */
    public void error(String correlationId, Exception error) {
        formatAndWrite(LogLevel.Error, correlationId, error, null, null);
    }

    /**
     * Logs recoverable application error.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param error         an error object associated with this message.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    public void error(String correlationId, Exception error, String message, Object... args) {
        formatAndWrite(LogLevel.Error, correlationId, error, message, args);
    }

    /**
     * Logs a warning that may or may not have a negative impact.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    public void warn(String correlationId, String message, Object... args) {
        formatAndWrite(LogLevel.Warn, correlationId, null, message, args);
    }

    /**
     * Logs an important information message
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    public void info(String correlationId, String message, Object... args) {
        formatAndWrite(LogLevel.Info, correlationId, null, message, args);
    }

    /**
     * Logs a high-level debug information for troubleshooting.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    public void debug(String correlationId, String message, Object... args) {
        formatAndWrite(LogLevel.Debug, correlationId, null, message, args);
    }

    /**
     * Logs a low-level debug information for troubleshooting.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param message       a human-readable message to log.
     * @param args          arguments to parameterize the message.
     */
    public void trace(String correlationId, String message, Object... args) {
        formatAndWrite(LogLevel.Trace, correlationId, null, message, args);
    }
}

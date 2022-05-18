package org.pipservices3.components.trace;

import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IConfigurable;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.IReferenceable;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.components.log.CompositeLogger;
import org.pipservices3.components.log.LogLevel;
import org.pipservices3.components.log.LogLevelConverter;

/**
 * Tracer that dumps recorded traces to logger.
 *
 * ### Configuration parameters ###
 *
 * <ul>
 *     <li> - options:
 *     <ul>
 *         <li> - log_level:         log level to record traces (default: debug)
 *     </ul>
 * </ul>
 *
 * ### References ###
 * <ul>
 * <li>*:logger:*:*:1.0         (optional) {@link org.pipservices3.components.log.ILogger} components to dump the captured counters
 * <li>*:context-info:*:*:1.0         (optional) {@link org.pipservices3.components.info.ContextInfo} to detect the context id and specify counters source
 * </ul>
 *
 * @see ITracer
 * @see org.pipservices3.components.count.CachedCounters
 * @see CompositeLogger
 *
 * ### Example ###
 *
 * {@code
 * LogTracer tracer = new LogTracer();
 * tracer.setReferences(References.fromTuples(
 *     new Descriptor("pip-services", "logger", "console", "default", "1.0"), new ConsoleLogger()
 * ));
 *
 * TraceTiming timing = trcer.beginTrace("123", "mycomponent", "mymethod");
 * try {
 *     ...
 *     timing.endTrace();
 * } catch(err) {
 *     timing.endFailure(err);
 * }
 * }
 */
public class LogTracer implements IConfigurable, IReferenceable, ITracer {
    private final CompositeLogger _logger = new CompositeLogger();
    private LogLevel _logLevel = LogLevel.Debug;

    /**
     * Configures component by passing configuration parameters.
     *
     * @param config configuration parameters to be set.
     * @throws ConfigException
     */
    @Override
    public void configure(ConfigParams config) throws ConfigException {
        this._logLevel = LogLevelConverter.toLogLevel(
                config.getAsObject("options.log_level"),
                this._logLevel
        );
    }

    /**
     * Sets references to dependent components.
     *
     * @param references references to locate the component dependencies.
     * @throws ReferenceException
     * @throws ConfigException
     */
    @Override
    public void setReferences(IReferences references) {
        this._logger.setReferences(references);
    }

    private void logTrace(String correlationId, String component, String operation, Exception error, long duration) {
        StringBuilder builder = new StringBuilder();

        if (error != null) {
            builder.append("Failed to execute ");
        } else {
            builder.append("Executed ");
        }

        builder.append(component);

        if (operation != null && !operation.equals("")) {
            builder.append(".");
            builder.append(operation);
        }

        if (duration > 0) {
            builder.append(" in ").append(duration).append(" msec");
        }

        if (error != null) {
            this._logger.error(correlationId, String.valueOf(error), builder);
        } else {
            this._logger.log(this._logLevel, correlationId, null, String.valueOf(builder));
        }
    }

    /**
     * Records an operation trace with its name and duration
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param component     a name of called component
     * @param operation     a name of the executed operation.
     * @param duration      execution duration in milliseconds.
     */
    @Override
    public void trace(String correlationId, String component, String operation, Long duration) {
        this.logTrace(correlationId, component, operation, null, duration);
    }

    /**
     * Records an operation failure with its name, duration and error
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param component     a name of called component
     * @param operation     a name of the executed operation.
     * @param error         an error object associated with this trace.
     * @param duration      execution duration in milliseconds.
     */
    public void failure(String correlationId, String component, String operation, Exception error, long duration) {
        this.logTrace(correlationId, component, operation, error, duration);
    }

    /**
     * Begings recording an operation trace
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param component     a name of called component
     * @param operation     a name of the executed operation.
     * @returns a trace timing object.
     */
    public TraceTiming beginTrace(String correlationId, String component, String operation) {
        return new TraceTiming(correlationId, component, operation, this);
    }

}

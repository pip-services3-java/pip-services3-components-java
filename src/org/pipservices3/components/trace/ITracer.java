package org.pipservices3.components.trace;

/**
 * Interface for tracer components that capture operation traces.
 */
public interface ITracer {
    void trace(String correlationId, String component, String operation, Long duration);

    void failure(String correlationId, String component, String operation, Exception error, long duration);

    TraceTiming beginTrace(String correlationId, String component, String operation);
}

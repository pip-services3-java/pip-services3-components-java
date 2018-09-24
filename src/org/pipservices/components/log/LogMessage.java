package org.pipservices.components.log;

import java.time.*;

import org.pipservices.commons.errors.*;

import com.fasterxml.jackson.annotation.*;

/**
 * Data object to store captured log messages.
 * This object is used by CachedLogger.
 */
public class LogMessage {
	/** The time then message was generated */
	private ZonedDateTime _time;
	/** The source (context name) */
	private String _source;
	/** This log level */
	private LogLevel _level;
	/** The transaction id to trace execution through call chain. */
	private String _correlationId;
	/**
	 * The description of the captured error
	 * @see ErrorDescription
	 */
	private ErrorDescription _error;
	/** The human-readable message */
	private String _message;
	
    public LogMessage() { }

    public LogMessage(LogLevel level, String source, String correlationId, ErrorDescription error, String message) {
    	_time = ZonedDateTime.now(ZoneId.of("Z"));
        _level = level;
        _source = source;
        _correlationId = correlationId;
        _error = error;
        _message = message;
    }

    @JsonProperty("time")
    public ZonedDateTime getTime() { return _time; }
    public void setTime(ZonedDateTime value) { _time = value; }

    @JsonProperty("source")
    public String getSource() { return _source; }
    public void setSource(String value) { _source = value; }

    @JsonProperty("level")
    public LogLevel getLevel() { return _level; }
    public void setLevel(LogLevel value) { _level = value; }

    @JsonProperty("correlation_id")
    public String getCorrelationId() { return _correlationId; }
    public void setCorrelationId(String value) { _correlationId = value; }

    @JsonProperty("error")
    public ErrorDescription getError() { return _error; }
    public void setError(ErrorDescription value) { _error = value; }
    
    @JsonProperty("message")
    public String getMessage() { return _message; }
    public void setMessage(String value) { _message = value; }
}

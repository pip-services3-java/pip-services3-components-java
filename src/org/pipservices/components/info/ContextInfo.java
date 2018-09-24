package org.pipservices.components.info;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.pipservices.commons.config.*;
import org.pipservices.commons.data.StringValueMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Context information component that provides detail information
 * about execution context: container or/and process.
 * 
 * Most often ContextInfo is used by logging and performance counters
 * to identify source of the collected logs and metrics.
 * 
 * ### Configuration parameters ###
 * 
 * name: 					the context (container or process) name
 * description: 		   	human-readable description of the context
 * properties: 			entire section of additional descriptive properties
 * 	 ...
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ContextInfo contextInfo = new ContextInfo();
 * contextInfo.configure(ConfigParams.fromTuples(
 * 		"name", "MyMicroservice",
 * 		"description", "My first microservice"
 * ));
 * 
 * context.name;			// Result: "MyMicroservice"
 * context.contextId;		// Possible result: "mylaptop"
 * context.startTime;		// Possible result: 2018-01-01:22:12:23.45Z
 * context.uptime;			// Possible result: 3454345
 * }
 * </pre>
 */
public final class ContextInfo implements IReconfigurable {

	private String _name = "unknown";
	private StringValueMap _properties = new StringValueMap();
	private String _description;
	private String contextId;
	private ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC"));
	private long uptime;
	private List<String> components;

	/**
	 * Creates a new instance of this context info.
	 */
	public ContextInfo() {
	}

	/**
	 * Creates a new instance of this context info.
	 * 
	 * @param name        (optional) a context name.
	 * @param description (optional) a human-readable description of the context.
	 */
	public ContextInfo(String name, String description) {
		setName(name);
		setDescription(description);
	}

	/**
	 * Configures component by passing configuration parameters.
	 * 
	 * @param config configuration parameters to be set.
	 */
	@Override
	public void configure(ConfigParams config) {
		_name = config.getAsStringWithDefault("name", _name);
		_name = config.getAsStringWithDefault("info.name", _name);

		_description = config.getAsStringWithDefault("description", _description);
		_description = config.getAsStringWithDefault("info.description", _description);

		_properties = config.getSection("properties");

	}

	/**
	 * Gets the context name.
	 * 
	 * @return the context name
	 */
	@JsonProperty("name")
	public String getName() {
		return _name;
	}

	/**
	 * Sets the context name.
	 * 
	 * @param _name a new name for the context.
	 */
	public void setName(String _name) {
		this._name = _name != null ? _name : "unknown";
	}

	/**
	 * Gets the human-readable description of the context.
	 * 
	 * @return the human-readable description of the context.
	 */
	@JsonProperty("description")
	public String getDescription() {
		return _description;
	}

	/**
	 * Sets the human-readable description of the context.
	 * 
	 * @param _description a new human readable description of the context.
	 */
	public void setDescription(String _description) {
		this._description = _description;
	}

	/**
	 * Gets the unique context id. Usually it is the current host name.
	 * 
	 * @return the unique context id.
	 */
	@JsonProperty("context_id")
	public String getContextId() {
		return contextId;
	}

	/**
	 * Sets the unique context id.
	 * 
	 * @param contextId a new unique context id.
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * Gets the context start time.
	 * 
	 * @return the context start time.
	 */
	@JsonProperty("start_time")
	public ZonedDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Sets the context start time.
	 * 
	 * @param startTime a new context start time.
	 */
	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Calculates the context uptime as from the start time.
	 * 
	 * @return number of milliseconds from the context start time.
	 */
	@JsonProperty("uptime")
	public long getUptime() {
		return uptime;
	}

	/**
	 * Sets context uptime parameter.
	 * 
	 * @param uptime a new uptime parameter.
	 */
	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	/**
	 * Gets context additional parameters.
	 * 
	 * @return a JSON object with additional context parameters.
	 */
	@JsonProperty("properties")
	public StringValueMap getProperties() {
		return _properties;
	}

	/**
	 * Sets context additional parameters.
	 * 
	 * @param _properties a JSON object with context additional parameters
	 */
	public void setProperties(StringValueMap _properties) {
		this._properties = _properties;
	}

	/**
	 * Gets a list of context components.
	 * 
	 * @return context components.
	 */
	@JsonProperty("components")
	public List<String> getComponents() {
		return components;
	}

	/**
	 * Sets context components.
	 * 
	 * @param components a new list of context components.
	 */
	public void setComponents(List<String> components) {
		this.components = components;
	}

	/**
	 * Creates a new ContextInfo and sets its configuration parameters.
	 * 
	 * @param config configuration parameters for the new ContextInfo.
	 * @return a newly created ContextInfo
	 */
	public static ContextInfo fromConfig(ConfigParams config) {
		ContextInfo result = new ContextInfo();
		result.configure(config);
		return result;
	}

}

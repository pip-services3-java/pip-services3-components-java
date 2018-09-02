package org.pipservices.components.info;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.pipservices.commons.config.*;
import org.pipservices.commons.data.StringValueMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ContextInfo implements IReconfigurable{
	
	private String _name = "unknown";
    private StringValueMap _properties = new StringValueMap();
	private String _description;
	private String contextId;
	private ZonedDateTime startTime = ZonedDateTime.now(ZoneId.of("UTC"));
	private long uptime;
	private List<String> components;

    public ContextInfo() { }
    
    public ContextInfo(String name, String description )
    {
        setName(name);
        setDescription(description);
    }
    
    @JsonProperty("name")
	public String getName() { return _name;	}
	public void setName(String _name) { this._name = _name != null ? _name : "unknown";	}

	@JsonProperty("description")
	public String getDescription() { return _description;	}
	public void setDescription(String _description) { this._description = _description; }

	@JsonProperty("context_id")
	public String getContextId() {	return contextId;	}
	public void setContextId(String contextId) { this.contextId = contextId;	}

	@JsonProperty("start_time")
	public ZonedDateTime getStartTime() { return startTime;	}
	public void setStartTime(ZonedDateTime startTime) { this.startTime = startTime;	}

	@JsonProperty("uptime")
	public long getUptime() { return uptime;	}
	public void setUptime(long uptime) { this.uptime = uptime;	}

	@JsonProperty("properties")
	public StringValueMap getProperties() { return _properties;	}
	public void setProperties(StringValueMap _properties) { this._properties = _properties;	}
	
	@JsonProperty("components")
	public List<String> getComponents() { return components; }
	public void setComponents(List<String> components) { this.components = components; }

	@Override
	public void configure(ConfigParams config) {
        _name = config.getAsStringWithDefault("name", _name);
        _name = config.getAsStringWithDefault("info.name", _name);

        _description = config.getAsStringWithDefault("description", _description);
        _description = config.getAsStringWithDefault("info.description", _description);

        _properties = config.getSection("properties");
		
	}
	
	public static ContextInfo fromConfig(ConfigParams config)
    {
		ContextInfo result = new ContextInfo();
        result.configure(config);
        return result;
    }

}

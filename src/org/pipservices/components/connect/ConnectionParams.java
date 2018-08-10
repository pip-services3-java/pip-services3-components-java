package org.pipservices.components.connect;

import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.data.*;

/**
 * Connection parameters as set in component configuration or retrieved by discovery service. 
 * It contains service protocol, host, port number, route, database name, timeouts 
 * and additional configuration parameters.
 */
public class ConnectionParams extends ConfigParams {
	private static final long serialVersionUID = 5769508200513539527L;

	/**
	 * Creates an empty instance of connection parameters.
	 */
	public ConnectionParams() { }
	
	/**
	 * Create an instance of service address with free-form configuration map.
	 * @param map a map with the address configuration parameters. 
	 */
	public ConnectionParams(Map<?, ?> map) {
		super(map);
	}

	/**
	 * Checks if discovery registration or resolution shall be performed.
	 * The discovery is requested when 'discover' parameter contains 
	 * a non-empty string that represents the discovery name.
	 * @return <b>true</b> if the address shall be handled by discovery 
	 * and <b>false</b> when all address parameters are defined statically.
	 */
	public boolean useDiscovery() {
		return containsKey("discovery_key");
	}
	
	/**
	 * Gets a key under which the connection shall be registered or resolved by discovery service. 
	 * @return a key to register or resolve the connection
	 */
	public String getDiscoveryKey() {
		return getAsNullableString("discovery_key");
	}

	/**
	 * Sets the key under which the connection shall be registered or resolved by discovery service
	 * @param value a key to register or resolve the connection
	 */
	public void setDiscoveryKey(String value) {
		put("discovery_key", value);
	}
	
	/**
	 * Gets the connection protocol
	 * @return the connection protocol
	 */
	public String getProtocol() {
		return getAsNullableString("protocol");
	}

	/**
	 * Gets the connection protocol
	 * @param defaultValue the default protocol
	 * @return the connection protocol
	 */
	public String getProtocol(String defaultValue) {
		return getAsStringWithDefault("protocol", defaultValue);
	}

	/**
	 * Sets the connection protocol
	 * @param value the connection protocol
	 */
	public void setProtocol(String value) {
		put("protocol", value);
	}

	/**
	 * Gets the service host name or IP address.
	 * @return a string representing service host 
	 */
	public String getHost() {
		String host = getAsNullableString("host");
		host = host != null ? host : getAsNullableString("ip");
		return host;
	}

	/**
	 * Sets the service host name or IP address.
	 * @param value a string representing service host 
	 */
	public void setHost(String value) {
		put("host", value);
	}

	/**
	 * Gets the service port number
	 * @return integer representing the service port.
	 */
	public int getPort() {
	    return getAsIntegerWithDefault("port", 8080);           
	}

	/**
	 * Sets the service port number
	 * @param value integer representing the service port.
	 */
	public void setPort(int value) {
		setAsObject("port", value);
	}

	/**
	 * Gets the endpoint uri constructed from protocol, host and port
	 * @return uri as <protocol>://<host | ip>:<port>
	 */
	public String getUri() {
		if (getProtocol() == null)
			setProtocol("http");
		
		if (getHost() == null)
			setHost("localhost");
		
		return getProtocol() + "://" + getHost() + ":" + getPort();
	}
	
	public void setUri(String value) {
		put("uri", value);
	}
	
	public static ConnectionParams fromString(String line) {
		StringValueMap map = StringValueMap.fromString(line);
		return new ConnectionParams(map);
	}

    public static List<ConnectionParams> manyFromConfig(ConfigParams config, boolean configAsDefault) {
        List<ConnectionParams> result = new ArrayList<ConnectionParams>();

        // Try to get multiple connections first
        ConfigParams connections = config.getSection("connections");

        if (connections.size() > 0) {
            List<String> connectionSections = connections.getSectionNames();

            for (String section : connectionSections) {
                ConfigParams connection = connections.getSection(section);
                result.add(new ConnectionParams(connection));
            }
        }
        // Then try to get a single connection
        else {
            ConfigParams connection = config.getSection("connection");
            if (connection.size() > 0)
                result.add(new ConnectionParams(connection));
            // Apply default if possible
            else if (configAsDefault)
                result.add(new ConnectionParams(config));
        }

        return result;
    }

    public static List<ConnectionParams> manyFromConfig(ConfigParams config) {
    	return manyFromConfig(config, true);
    }

    public static ConnectionParams fromConfig(ConfigParams config, boolean configAsDefault) {
        List<ConnectionParams> connections = manyFromConfig(config, configAsDefault);
        return connections.size() > 0 ? connections.get(0) : null;
    }

    public static ConnectionParams fromConfig(ConfigParams config) {
    	return fromConfig(config, true);
    }
    
}

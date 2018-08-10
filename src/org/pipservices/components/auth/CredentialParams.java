package org.pipservices.components.auth;

import java.util.*;

import org.pipservices.commons.config.ConfigParams;
import org.pipservices.commons.data.*;

/**
 * Credentials such as login and password, client id and key,
 * certificates, etc. Separating credentials from connection parameters
 * allow to store them in secure location and share among multiple
 * connections.
 */
public class CredentialParams extends ConfigParams {
	private static final long serialVersionUID = 4144579662501676747L;

	/**
	 * Creates an empty instance of credential parameters.
	 */
	public CredentialParams() { }
	
	/**
	 * Create an instance of credentials from free-form configuration map.
	 * @param map a map with the credentials. 
	 */
	public CredentialParams(Map<?, ?> map) {
		super(map);
	}

	/**
	 * Checks if credential lookup shall be performed.
	 * The credentials are requested when 'store_key' parameter contains 
	 * a non-empty string that represents the name in credential store.
	 * @return <b>true</b> if the credentials shall be resolved by credential store 
	 * and <b>false</b> when all credential parameters are defined statically.
	 */
	public boolean useCredentialStore() {
		return containsKey("store_key");
	}
	
	/**
	 * Gets a key under which the connection shall be looked up in credential store. 
	 * @return a credential key
	 */
	public String getStoreKey() {
		return getAsNullableString("store_key");
	}

	/**
	 * Sets the key under which the credentials shall be looked up in credential store
	 * @param value a new credential key
	 */
	public void setStoreKey(String value) {
		put("store_key", value);
	}
	
	/**
	 * Gets the user name / login.
	 * @return the user name 
	 */
	public String getUsername() {
		return getAsNullableString("username");
	}

	/**
	 * Sets the service user name.
	 * @param value the user name 
	 */
	public void setUsername(String value) {
		put("username", value);
	}

	/**
	 * Gets the service user password.
	 * @return the user password 
	 */
	public String getPassword() {
		return getAsNullableString("password");
	}

	/**
	 * Sets the service user password.
	 * @param the user password 
	 */
	public void setPassword(String password) {
		put("password", password);
	}
	
	/**
	 * Gets the client or access id
	 * @return the client or access id
	 */
	public String getAccessId() {
		String accessId = getAsNullableString("access_id");
		accessId = accessId != null ? accessId : getAsNullableString("client_id");
		return accessId;
	}

	/**
	 * Sets a new client or access id
	 * @param value the client or access id
	 */
	public void setAccessId(String value) {
		put("access_id", value);
	}

	/**
	 * Gets the client or access key
	 * @return the client or access key
	 */
	public String getAccessKey() {
		String accessKey = getAsNullableString("access_key");
		accessKey = accessKey != null ? accessKey : getAsNullableString("client_key");
		return accessKey;
	}

	/**
	 * Sets a new client or access key
	 * @param value the client or access id
	 */
	public void setAccessKey(String value) {
		put("access_key", value);
	}
	
	public static CredentialParams fromString(String line) {
		StringValueMap map = StringValueMap.fromString(line);
		return new CredentialParams(map);
	}

    public static List<CredentialParams> manyFromConfig(ConfigParams config, boolean configAsDefault) {
        List<CredentialParams> result = new ArrayList<CredentialParams>();

        // Try to get multiple credentials first
        ConfigParams credentials = config.getSection("credentials");

        if (credentials.size() > 0) {
            List<String> sectionsNames = credentials.getSectionNames();

            for (String section : sectionsNames) {
                ConfigParams credential = credentials.getSection(section);
                result.add(new CredentialParams(credential));
            }
        }
        // Then try to get a single connection
        else {
            ConfigParams credential = config.getSection("credential");
            if (credential.size() > 0)
                result.add(new CredentialParams(credential));
            // Apply defaults
            else if (configAsDefault)
                result.add(new CredentialParams(config));
        }

        return result;
    }

    public static List<CredentialParams> manyFromConfig(ConfigParams config) {
    	return manyFromConfig(config, true);
    }

    public static CredentialParams fromConfig(ConfigParams config, boolean configAsDefault) {
        List<CredentialParams> connections = manyFromConfig(config, configAsDefault);
        return connections.size() > 0 ? connections.get(0) : null;
    }

    public static CredentialParams fromConfig(ConfigParams config) {
    	return fromConfig(config, true);
    }

}

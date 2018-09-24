package org.pipservices.components.auth;

import java.util.*;

import org.pipservices.commons.config.*;

/**
 * Credential store that keeps credentials in memory.
 * 
 * ### Configuration parameters ###
 * 
 * [credential key 1]:            
 *   ...                          credential parameters for key 1
 * [credential key 2]:            
 *   ...                          credential parameters for key N
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ConfigParams config = ConfigParams.fromTuples(
 *      "key1.user", "jdoe",
 *      "key1.pass", "pass123",
 *      "key2.user", "bsmith",
 *      "key2.pass", "mypass"
 * );
 *
 * MemoryCredentialStore credentialStore = new MemoryCredentialStore();
 * credentialStore.readCredentials(config);
 * 
 * credentialStore.lookup("123", "key1", (err, credential) => {
 *      // Result: user=jdoe;pass=pass123
 * });
 * }
 * </pre>
 * @see ICredentialStore
 * @see CredentialParams
 */
public class MemoryCredentialStore implements ICredentialStore, IReconfigurable {

	private Map<String, CredentialParams> _items = new HashMap<String, CredentialParams>();
	private Object _lock = new Object();

	/**
	 * Creates a new instance of the credential store.
	 */
	public MemoryCredentialStore() {
	}

	/**
	 * Creates a new instance of the credential store.
	 * 
	 * @param credentials (optional) configuration with credential parameters.
	 */
	public MemoryCredentialStore(ConfigParams credentials) {
		configure(credentials);
	}

	/**
	 * Configures component by passing configuration parameters.
	 * 
	 * @param config configuration parameters to be set.
	 */
	public void configure(ConfigParams config) {
		readCredentials(config);
	}

	public void readCredentials(ConfigParams credentials) {
		synchronized (_lock) {
			_items.clear();
			for (Map.Entry<String, String> entry : credentials.entrySet())
				_items.put(entry.getKey(), CredentialParams.fromString(entry.getValue()));
		}
	}

	/**
	 * Stores credential parameters into the store.
	 *
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the credential parameters.
	 * @param credential    a credential parameters to be stored.
	 */
	public void store(String correlationId, String key, CredentialParams credential) {
		synchronized (_lock) {
			if (credential != null)
				_items.put(key, credential);
			else
				_items.remove(key);
		}
	}

	/**
	 * Lookups credential parameters by its key.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the credential parameters.
	 * @return resolved credential parameters or null if nothing was found.
	 */
	public CredentialParams lookup(String correlationId, String key) {
		synchronized (_lock) {
			return _items.get(key);
		}
	}

}

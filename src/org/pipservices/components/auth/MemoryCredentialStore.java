package org.pipservices.components.auth;

import java.util.*;

import org.pipservices.commons.config.*;

public class MemoryCredentialStore implements ICredentialStore, IReconfigurable {
	
    private Map<String, CredentialParams> _items = new HashMap<String, CredentialParams>();
    private Object _lock = new Object();

    public MemoryCredentialStore() { }

    public MemoryCredentialStore(ConfigParams credentials) {
        configure(credentials);
    }

    public void configure(ConfigParams config) {
        readCredentials(config);
    }

    private void readCredentials(ConfigParams credentials) {
        synchronized (_lock) {
            _items.clear();
            for (Map.Entry<String, String> entry : credentials.entrySet())
                _items.put(entry.getKey(), CredentialParams.fromString(entry.getValue()));
        }
    }

    public void store(String correlationId, String key, CredentialParams credential) {
        synchronized (_lock) {
            if (credential != null)
                _items.put(key, credential);
            else
                _items.remove(key);
        }
    }

    public CredentialParams lookup(String correlationId, String key) {
        synchronized (_lock) {
            return _items.get(key);
        }
    }

}

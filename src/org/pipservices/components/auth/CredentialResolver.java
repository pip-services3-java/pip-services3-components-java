package org.pipservices.components.auth;

import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;

public class CredentialResolver implements IConfigurable, IReferenceable {
	private List<CredentialParams> _credentials = new ArrayList<CredentialParams>();
	private IReferences _references = null;

	public CredentialResolver() {}
	
	public CredentialResolver(ConfigParams config) {
		configure(config);
	}

	public CredentialResolver(ConfigParams config, IReferences references) {
		configure(config);
		setReferences(references);
	}

	public void setReferences(IReferences references) {
		_references = references;
	}

	public void configure(ConfigParams config, boolean configAsDefault) {
        _credentials.addAll(CredentialParams.manyFromConfig(config, configAsDefault));
	}

	public void configure(ConfigParams config) {
		configure(config, true);
	}

	public List<CredentialParams> getAll() {
		return _credentials;
	}
	
	public void add(CredentialParams connection) {
		_credentials.add(connection);
	}

	private CredentialParams lookupInStores(String correlationId, CredentialParams credential) 
		throws ApplicationException {
		
		if (credential.useCredentialStore() == false) return null;
		
		String key = credential.getStoreKey();
		if (_references == null)
			return null;
		
		List<Object> components = _references.getOptional(new Descriptor("*", "credential_store", "*", "*", "*"));
		if (components.size() == 0)
			throw new ReferenceException(correlationId, "Credential store wasn't found to make lookup");

		for (Object component : components) {
			if (component instanceof ICredentialStore) {
				CredentialParams resolvedCredential = ((ICredentialStore)component).lookup(correlationId, key);
				if (resolvedCredential != null)
					return resolvedCredential;
			}
		}
		
		return null;
	}

	public CredentialParams lookup(String correlationId) throws ApplicationException {		
		if (_credentials.size() == 0) return null;
		
		// Return connection that doesn't require discovery
		for (CredentialParams credential : _credentials) {
			if (!credential.useCredentialStore())
				return credential;
		}
		
		// Return connection that require discovery
		for (CredentialParams credential : _credentials) {
			if (credential.useCredentialStore()) {
				CredentialParams resolvedConnection = lookupInStores(correlationId, credential);
				if (resolvedConnection != null)
					return resolvedConnection;
			}
		}
		
		return null;
	}
	
}

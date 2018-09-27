package org.pipservices.components.auth;

import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;

/**
 * Helper class to retrieve component credentials.
 * 
 * If credentials are configured to be retrieved from ICredentialStore,
 * it automatically locates ICredentialStore in component references
 * and retrieve credentials from there using store_key parameter.
 * 
 * ### Configuration parameters ###
 * 
 * credential:    
 *   store_key:                   (optional) a key to retrieve the credentials from [[ICredentialStore]]
 *   ...                          other credential parameters
 * 
 * credentials:                   alternative to credential
 *   [credential params 1]:       first credential parameters
 *     ...
 *   [credential params N]:       Nth credential parameters
 *     ...
 * 
 * ### References ###
 * 
 * - *:credential-store:*:*:1.0     (optional) Credential stores to resolve credentials
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ConfigParams config = ConfigParams.fromTuples(
 *      "credential.user", "jdoe",
 *      "credential.pass",  "pass123"
 * );
 * 
 * CredentialResolver credentialResolver = new CredentialResolver();
 * credentialResolver.configure(config);
 * credentialResolver.setReferences(references);
 * 
 * credentialResolver.lookup("123");
 * }
 * </pre>
 * @see CredentialParams
 * @see ICredentialStore
 */
public class CredentialResolver implements IConfigurable, IReferenceable {
	private List<CredentialParams> _credentials = new ArrayList<CredentialParams>();
	private IReferences _references = null;

	/**
	 * Creates a new instance of credentials resolver.
	 */
	public CredentialResolver() {
	}

	/**
	 * Creates a new instance of credentials resolver.
	 * 
	 * @param config (optional) component configuration parameter.
	 */
	public CredentialResolver(ConfigParams config) {
		configure(config);
	}

	/**
	 * Creates a new instance of credentials resolver.
	 * 
	 * @param config     (optional) component configuration parameters
	 * @param references (optional) component references
	 */
	public CredentialResolver(ConfigParams config, IReferences references) {
		configure(config);
		setReferences(references);
	}

	/**
	 * Configures component by passing configuration parameters.
	 * 
	 * @param config          configuration parameters to be set.
	 * @param configAsDefault boolean parameter for default configuration. If "true"
	 *                        the default value will be added to the result.
	 */
	public void configure(ConfigParams config, boolean configAsDefault) {
		_credentials.addAll(CredentialParams.manyFromConfig(config, configAsDefault));
	}

	/**
	 * Configures component by passing configuration parameters.
	 * 
	 * @param config configuration parameters to be set.
	 */
	public void configure(ConfigParams config) {
		configure(config, true);
	}

	/**
	 * Sets references to dependent components.
	 * 
	 * @param references references to locate the component dependencies.
	 */
	public void setReferences(IReferences references) {
		_references = references;
	}

	/**
	 * Gets all credentials configured in component configuration.
	 * 
	 * Redirect to CredentialStores is not done at this point. If you need fully
	 * fleshed credential use lookup() method instead.
	 * 
	 * @return a list with credential parameters
	 */
	public List<CredentialParams> getAll() {
		return _credentials;
	}

	/**
	 * Adds a new credential to component credentials
	 * 
	 * @param connection new credential parameters to be added
	 */
	public void add(CredentialParams connection) {
		_credentials.add(connection);
	}

	private CredentialParams lookupInStores(String correlationId, CredentialParams credential)
			throws ApplicationException {

		if (credential.useCredentialStore() == false)
			return null;

		String key = credential.getStoreKey();
		if (_references == null)
			return null;

		List<Object> components = _references.getOptional(new Descriptor("*", "credential_store", "*", "*", "*"));
		if (components.size() == 0)
			throw new ReferenceException(correlationId, "Credential store wasn't found to make lookup");

		for (Object component : components) {
			if (component instanceof ICredentialStore) {
				CredentialParams resolvedCredential = ((ICredentialStore) component).lookup(correlationId, key);
				if (resolvedCredential != null)
					return resolvedCredential;
			}
		}

		return null;
	}

	/**
	 * Looks up component credential parameters. If credentials are configured to be
	 * retrieved from Credential store it finds a ICredentialStore and lookups
	 * credentials there.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @return resolved credential parameters or null if nothing was found.
	 * @throws ApplicationException when errors occured.
	 */
	public CredentialParams lookup(String correlationId) throws ApplicationException {
		if (_credentials.size() == 0)
			return null;

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

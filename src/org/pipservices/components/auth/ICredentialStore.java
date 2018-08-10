package org.pipservices.components.auth;

/**
 * Store that keeps and located client credentials.
 */
public interface ICredentialStore {
	/**
	 * Stores credential in the store
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key the key to lookup credential
	 * @param credential a credential parameters
	 */
	void store(String correlationId, String key, CredentialParams credential);
	
	/**
	 * Looks up credential from the store
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a key to lookup credential
	 * @return found credential parameters or <code>null</code> if nothing was found
	 */
	CredentialParams lookup(String correlationId, String key);
}

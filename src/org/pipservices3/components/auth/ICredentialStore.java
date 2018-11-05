package org.pipservices3.components.auth;

import org.pipservices3.components.connect.*;
/**
 * Interface for credential stores which are used to store and lookup credentials
 * to authenticate against external services.
 * 
 * @see CredentialParams
 * @see ConnectionParams
 */
public interface ICredentialStore {
	/**
	 * Stores credential parameters into the store.
	 *
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the credential.
	 * @param credential    a credential to be stored.
	 */
	void store(String correlationId, String key, CredentialParams credential);

	/**
	 * Lookups credential parameters by its key.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the credential.
	 * @return found credential parameters or null if nothing was found
	 */
	CredentialParams lookup(String correlationId, String key);
}

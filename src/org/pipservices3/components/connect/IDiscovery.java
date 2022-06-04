package org.pipservices3.components.connect;

import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.components.auth.CredentialParams;

import java.util.List;

/**
 * Interface for discovery services which are used to store and resolve connection parameters
 * to connect to external services.
 * 
 * @see ConnectionParams
 * @see CredentialParams
 */
public interface IDiscovery {
	/**
	 * Registers connection parameters into the discovery service.
	 *
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the connection parameters.
	 * @param connection    a connection to be registered.
	 * @throws ApplicationException when registration fails for whatever reasons
	 */
	void register(String correlationId, String key, ConnectionParams connection) throws ApplicationException;

	/**
	 * Resolves a single connection parameters by its key.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the connection.
	 * @return a resolved connection.
	 * @throws ApplicationException when resolution failed for whatever reasons.
	 */
	ConnectionParams resolveOne(String correlationId, String key) throws ApplicationException;

	/**
	 * Resolves all connection parameters by their key.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the connections.
	 * @return a list with resolved connections.
	 * @throws ApplicationException when resolution failed for whatever reasons.
	 */
	List<ConnectionParams> resolveAll(String correlationId, String key) throws ApplicationException;
}

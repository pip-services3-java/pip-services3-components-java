package org.pipservices.components.connect;

import java.util.*;

import org.pipservices.commons.errors.*;

/**
 * Service discovery component used to register connections of the services
 * or to resolve connections to external services called by clients.
 */
public interface IDiscovery {
	/**
	 * Registers connection where API service binds to.
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a key to identify the connection
	 * @param connection the connection to be registered.
	 * @throws ApplicationException when registration fails for whatever reasons
	 */
	void register(String correlationId, String key, ConnectionParams connection) throws ApplicationException;
	
	/**
	 * Resolves one connection from the list of service connections.
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a key locate a connection
	 * @return a resolved connection.
	 * @throws ApplicationException when resolution failed for whatever reasons.
	 */
	ConnectionParams resolveOne(String correlationId, String key) throws ApplicationException;

	/**
	 * Resolves a list of connections from to be called by a client.
	 * @param correlationId a unique transaction id to trace calls across components
	 * @param key a key locate connections
	 * @return a list with resolved connections.
	 * @throws ApplicationException when resolution failed for whatever reasons.
	 */
	List<ConnectionParams> resolveAll(String correlationId, String key) throws ApplicationException;
}

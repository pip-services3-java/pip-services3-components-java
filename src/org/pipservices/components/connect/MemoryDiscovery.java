package org.pipservices.components.connect;

import java.util.*;

import org.pipservices.commons.config.*;

/**
 * Discovery service that keeps connections in memory.
 * 
 * ### Configuration parameters ###
 * 
 * [connection key 1]:            
 *   ...                          connection parameters for key 1
 * [connection key 2]:            
 *   ...                          connection parameters for key N
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ConfigParams config = ConfigParams.fromTuples(
 *      "key1.host", "10.1.1.100",
 *      "key1.port", "8080",
 *      "key2.host", "10.1.1.100",
 *      "key2.port", "8082"
 * );
 *
 * MemoryDiscovery discovery = new MemoryDiscovery();
 * discovery.readConnections(config);
 * 
 * discovery.resolve("123", "key1", (err, connection) => {
 *      // Result: host=10.1.1.100;port=8080
 * });
 * }
 * </pre>
 * @see IDiscovery
 * @see ConnectionParams
 */
public class MemoryDiscovery implements IDiscovery, IReconfigurable {
	private List<DiscoveryItem> _items = new ArrayList<DiscoveryItem>();
	private Object _lock = new Object();

	/**
	 * Creates a new instance of discovery service.
	 */
	public MemoryDiscovery() {
	}

	/**
	 * Creates a new instance of discovery service.
	 * 
	 * @param config (optional) configuration with connection parameters.
	 */
	public MemoryDiscovery(ConfigParams config) {
		if (config != null)
			configure(config);
	}

	private class DiscoveryItem {
		public String key;
		public ConnectionParams connection;
	}

	/**
	 * Configures component by passing configuration parameters.
	 * 
	 * @param config configuration parameters to be set.
	 */
	public void configure(ConfigParams config) {
		readConnections(config);
	}

	/**
	 * Reads connections from configuration parameters. Each section represents an
	 * individual Connection params
	 * 
	 * @param connections configuration parameters to be read
	 */
	public void readConnections(ConfigParams connections) {
		synchronized (_lock) {
			_items.clear();
			for (Map.Entry<String, String> entry : connections.entrySet()) {
				DiscoveryItem item = new DiscoveryItem();
				item.key = entry.getKey();
				item.connection = ConnectionParams.fromString(entry.getValue());
				_items.add(item);
			}
		}
	}

	/**
	 * Registers connection parameters into the discovery service.
	 *
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the connection parameters.
	 * @param connection    a connection to be registered.
	 */
	public void register(String correlationId, String key, ConnectionParams connection) {
		synchronized (_lock) {
			DiscoveryItem item = new DiscoveryItem();
			item.key = key;
			item.connection = connection;
			_items.add(item);
		}
	}

	/**
	 * Resolves a single connection parameters by its key.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the connection.
	 * @return receives found connection.
	 */
	public ConnectionParams resolveOne(String correlationId, String key) {
		ConnectionParams connection = null;

		synchronized (_lock) {
			for (DiscoveryItem item : _items) {
				if (item.key == key && item.connection != null) {
					connection = item.connection;
					break;
				}
			}
		}

		return connection;
	}

	/**
	 * Resolves all connection parameters by their key.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param key           a key to uniquely identify the connections.
	 * @return receives found connections.
	 */
	public List<ConnectionParams> resolveAll(String correlationId, String key) {
		List<ConnectionParams> connections = new ArrayList<ConnectionParams>();

		synchronized (_lock) {
			for (DiscoveryItem item : _items) {
				if (item.key == key && item.connection != null)
					connections.add(item.connection);
			}
		}

		return connections;
	}
}

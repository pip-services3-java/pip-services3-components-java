package org.pipservices.components.connect;

import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;

public class ConnectionResolver implements IConfigurable, IReferenceable {
	private List<ConnectionParams> _connections = new ArrayList<ConnectionParams>();
	private IReferences _references = null;

	public ConnectionResolver() {}
	
	public ConnectionResolver(ConfigParams config) {
		configure(config);
	}

	public ConnectionResolver(ConfigParams config, IReferences references) {
		configure(config);
		setReferences(references);
	}

	public void setReferences(IReferences references) {
		_references = references;
	}

	public void configure(ConfigParams config, boolean configAsDefault) {
        _connections.addAll(ConnectionParams.manyFromConfig(config, configAsDefault));
	}

	public void configure(ConfigParams config) {
		configure(config, true);
	}
	
	public List<ConnectionParams> getAll() {
		return _connections;
	}
	
	public void add(ConnectionParams connection) {
		_connections.add(connection);
	}

	private boolean registerInDiscovery(String correlationId, ConnectionParams connection) 
		throws ApplicationException {
		
		if (connection.useDiscovery() == false) return false;
		
		String key = connection.getDiscoveryKey();
		if (_references == null)
			return false;
		
		List<Object> components = _references.getOptional(new Descriptor("*", "discovery", "*", "*", "*"));
		if (components == null)
			return false;
		
		for (Object component : components) {
			if (component instanceof IDiscovery) {
				((IDiscovery)component).register(correlationId, key, connection);
			}
		}
		return true;
	}

	public void register(String correlationId, ConnectionParams connection) throws ApplicationException {
		boolean result = registerInDiscovery(correlationId, connection);
		
		if (result)
			_connections.add(connection);
	}
	
	private ConnectionParams resolveInDiscovery(String correlationId, ConnectionParams connection) 
		throws ApplicationException {
		
		if (connection.useDiscovery() == false) return null;
		
		String key = connection.getDiscoveryKey();
		if (_references == null) return null;
		
		List<Object> components = _references.getOptional(new Descriptor("*", "discovery", "*", "*", "*"));
		if (components.size() == 0)
			throw new ConfigException(correlationId, "CANNOT_RESOLVE", "Discovery wasn't found to make resolution");

		for (Object component : components) {
			if (component instanceof IDiscovery) {
				ConnectionParams resolvedConnection = ((IDiscovery)component).resolveOne(correlationId, key);
				if (resolvedConnection != null)
					return resolvedConnection;
			}
		}
		
		return null;
	}

	public ConnectionParams resolve(String correlationId) throws ApplicationException {		
		if (_connections.size() == 0) return null;
		
		// Return connection that doesn't require discovery
		for (ConnectionParams connection : _connections) {
			if (!connection.useDiscovery())
				return connection;
		}
		
		// Return connection that require discovery
		for (ConnectionParams connection : _connections) {
			if (connection.useDiscovery()) {
				ConnectionParams resolvedConnection = resolveInDiscovery(correlationId, connection);
				if (resolvedConnection != null) {
					// Merge configured and new parameters
					resolvedConnection = new ConnectionParams(ConfigParams.mergeConfigs(connection, resolvedConnection));
					return resolvedConnection;
				}
			}
		}
		
		return null;
	}

	private List<ConnectionParams> resolveAllInDiscovery(String correlationId, ConnectionParams connection) 
		throws ApplicationException {
		
		List<ConnectionParams> result = new ArrayList<ConnectionParams>();
		
		if (connection.useDiscovery() == false) return result;
		
		String key = connection.getDiscoveryKey();
        if (_references == null) return null;

        List<Object> components = _references.getOptional(new Descriptor("*", "discovery", "*", "*", "*"));
		if (components.size() == 0)
			throw new ConfigException(correlationId, "CANNOT_RESOLVE", "Discovery wasn't found to make resolution");

		for (Object component : components) {
			if (component instanceof IDiscovery) {
				List<ConnectionParams> resolvedConnections = ((IDiscovery)component).resolveAll(correlationId, key);
				if (resolvedConnections != null)
					result.addAll(resolvedConnections);
			}
		}
		
		return result;
	}
	
	public List<ConnectionParams> resolveAll(String correlationId) throws ApplicationException {
		List<ConnectionParams> resolved = new ArrayList<ConnectionParams>();
		List<ConnectionParams> toResolve = new ArrayList<ConnectionParams>();

		// Sort connections
		for (ConnectionParams connection : _connections) {
			if (connection.useDiscovery())
				toResolve.add(connection);
			else
				resolved.add(connection);
		}
		
		// Resolve addresses that require that
		if (toResolve.size() > 0) {
			for (ConnectionParams connection : toResolve) {
				List<ConnectionParams> resolvedConnections = resolveAllInDiscovery(correlationId, connection);
				for (ConnectionParams resolvedConnection : resolvedConnections) {
					// Merge configured and new parameters
					resolvedConnection = new ConnectionParams(ConfigParams.mergeConfigs(connection, resolvedConnection));
					resolved.add(resolvedConnection);
				}
			}
		}
		
		return resolved;
	}
	
}

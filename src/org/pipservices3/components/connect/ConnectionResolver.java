package org.pipservices3.components.connect;

import java.util.*;

import org.pipservices3.commons.config.*;
import org.pipservices3.commons.errors.*;
import org.pipservices3.commons.refer.*;

/**
 * Helper class to retrieve component connections.
 * <p>
 * If connections are configured to be retrieved from {@link IDiscovery},
 * it automatically locates {@link IDiscovery} in component references
 * and retrieve connections from there using <code>discovery_key</code> parameter.
 * <p>
 * ### Configuration parameters ###
 * <ul>
 * <li>connection:
 *   <ul>
 *   <li>discovery_key:               (optional) a key to retrieve the connection from {@link IDiscovery}
 *   <li>...                          other connection parameters
 *   </ul>
 * <li>connections:                   alternative to connection
 *   <ul>
 *   <li>[connection params 1]:       first connection parameters
 *   <li>...
 *   <li>[connection params N]:       Nth connection parameters
 *   <li>...
 *   </ul>
 * </ul>
 * <p>
 * ### References ###
 * <ul>
 * <li>*:discovery:*:*:1.0            (optional) {@link IDiscovery} services to resolve connections
 * </ul>
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ConfigParams config = ConfigParams.fromTuples(
 *      "connection.host", "10.1.1.100",
 *      "connection.port", 8080
 * );
 *
 * ConnectionResolver connectionResolver = new ConnectionResolver();
 * connectionResolver.configure(config);
 * connectionResolver.setReferences(references);
 *
 * connectionResolver.resolve("123");
 * }
 * </pre>
 *
 * @see ConnectionParams
 * @see IDiscovery
 */
public class ConnectionResolver implements IConfigurable, IReferenceable {
    private final List<ConnectionParams> _connections = new ArrayList<ConnectionParams>();
    private IReferences _references = null;

    /**
     * Creates a new instance of connection resolver.
     */
    public ConnectionResolver() {
    }

    /**
     * Creates a new instance of connection resolver.
     *
     * @param config (optional) component configuration parameters
     */
    public ConnectionResolver(ConfigParams config) {
        configure(config);
    }

    /**
     * Creates a new instance of connection resolver.
     *
     * @param config     (optional) component configuration parameters
     * @param references (optional) component references
     */
    public ConnectionResolver(ConfigParams config, IReferences references) {
        if (config != null)
            this.configure(config);

        if (references != null)
            this.setReferences(references);
    }

    /**
     * Configures component by passing configuration parameters.
     *
     * @param config          configuration parameters to be set.
     * @param configAsDefault boolean parameter for default configuration. If "true"
     *                        the default value will be added to the result.
     */
    public void configure(ConfigParams config, boolean configAsDefault) {
        _connections.addAll(ConnectionParams.manyFromConfig(config, configAsDefault));
    }

    /**
     * Configures component by passing configuration parameters.
     *
     * @param config configuration parameters to be set.
     */
    public void configure(ConfigParams config) {
        configure(config, false);
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
     * Gets all connections configured in component configuration.
     * <p>
     * Redirect to Discovery services is not done at this point. If you need fully
     * fleshed connection use resolve() method instead.
     *
     * @return a list with connection parameters
     */
    public List<ConnectionParams> getAll() {
        return _connections;
    }

    /**
     * Adds a new connection to component connections
     *
     * @param connection new connection parameters to be added
     */
    public void add(ConnectionParams connection) {
        _connections.add(connection);
    }

    private boolean registerInDiscovery(String correlationId, ConnectionParams connection) throws ApplicationException {

        if (!connection.useDiscovery())
            return false;

        String key = connection.getDiscoveryKey();
        if (_references == null)
            return false;

        List<Object> components = _references.getOptional(new Descriptor("*", "discovery", "*", "*", "*"));
        if (components == null)
            return false;

        for (Object component : components) {
            if (component instanceof IDiscovery) {
                ((IDiscovery) component).register(correlationId, key, connection);
            }
        }
        return true;
    }

    /**
     * Registers the given connection in all referenced discovery services. This
     * method can be used for dynamic service discovery.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param connection    a connection to register.
     * @throws ApplicationException when error occured.
     * @see IDiscovery
     */
    public void register(String correlationId, ConnectionParams connection) throws ApplicationException {
        boolean result = registerInDiscovery(correlationId, connection);

        if (result)
            _connections.add(connection);
    }

    private ConnectionParams resolveInDiscovery(String correlationId, ConnectionParams connection)
            throws ApplicationException {

        if (!connection.useDiscovery())
            return null;

        String key = connection.getDiscoveryKey();
        if (_references == null)
            return null;

        Descriptor discoveryDescriptor = new Descriptor("*", "discovery", "*", "*", "*");
        List<Object> components = _references.getOptional(discoveryDescriptor);
        if (components.size() == 0)
            throw new ReferenceException(correlationId, discoveryDescriptor);

        for (Object component : components) {
            if (component instanceof IDiscovery) {
                ConnectionParams resolvedConnection = ((IDiscovery) component).resolveOne(correlationId, key);
                if (resolvedConnection != null)
                    return resolvedConnection;
            }
        }

        return null;
    }

    /**
     * Resolves a single component connection. If connections are configured to be
     * retrieved from Discovery service it finds a IDiscovery and resolves the
     * connection there.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @return resolved connection parameters or null if nothing was found.
     * @throws ApplicationException when error occured.
     * @see IDiscovery
     */
    public ConnectionParams resolve(String correlationId) throws ApplicationException {
        if (_connections.size() == 0)
            return null;

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
                    resolvedConnection = new ConnectionParams(
                            ConfigParams.mergeConfigs(connection, resolvedConnection));
                    return resolvedConnection;
                }
            }
        }

        return null;
    }

    private List<ConnectionParams> resolveAllInDiscovery(String correlationId, ConnectionParams connection)
            throws ApplicationException {

        List<ConnectionParams> result = new ArrayList<ConnectionParams>();

        if (!connection.useDiscovery())
            return result;

        String key = connection.getDiscoveryKey();
        if (_references == null)
            return result;

        Descriptor discoveryDescriptor = new Descriptor("*", "discovery", "*", "*", "*");
        List<Object> components = _references.getOptional(new Descriptor("*", "discovery", "*", "*", "*"));
        if (components.size() == 0)
            throw new ReferenceException(correlationId, discoveryDescriptor);

        for (Object component : components) {
            if (component instanceof IDiscovery) {
                List<ConnectionParams> resolvedConnections = ((IDiscovery) component).resolveAll(correlationId, key);
                if (resolvedConnections != null)
                    result.addAll(resolvedConnections);
            }
        }

        return result;
    }

    /**
     * Resolves all component connection. If connections are configured to be
     * retrieved from Discovery service it finds a IDiscovery and resolves the
     * connection there.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @return a list of resolved connections.
     * @throws ApplicationException when error occured.
     * @see IDiscovery
     */
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
                List<ConnectionParams> results = resolveAllInDiscovery(correlationId, connection);
                for (ConnectionParams result : results) {
                    // Merge configured and new parameters
                    ConnectionParams localResolvedConnection = new ConnectionParams(ConfigParams.mergeConfigs(connection, result));
                    resolved.add(localResolvedConnection);
                }
            }
        }

        return resolved;
    }

}

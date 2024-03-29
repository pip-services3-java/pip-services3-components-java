package org.pipservices3.components.connect;


import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IConfigurable;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.IReferenceable;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.components.auth.CredentialParams;
import org.pipservices3.components.auth.CredentialResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that resolves connection and credential parameters,
 * validates them and generates connection options.
 * <p>
 * ### Configuration parameters ###
 * <p>
 * <ul>
 * <li> - connection(s):
 * <ul>
 *     <li> - discovery_key:               (optional) a key to retrieve the connection from {@link IDiscovery}
 *     <li> - protocol:                    communication protocol
 *     <li> - host:                        host name or IP address
 *     <li> - port:                        port number
 *     <li> - uri:                         resource URI or connection string with all parameters in it
 * </ul>
 * <li> - credential(s):
 * <ul>
 *     <li> - store_key:                   (optional) a key to retrieve the credentials from {@link org.pipservices3.components.auth.ICredentialStore}
 *     <li> - username:                    user name
 *     <li> - password:                    user password
 * </ul>
 * </ul>
 * <p>
 * <p>
 *  In addition to standard parameters CredentialParams may contain any number of custom parameters
 *
 * @see org.pipservices3.commons.config.ConfigParams
 * @see ConnectionParams
 * @see org.pipservices3.components.auth.CredentialResolver
 * @see org.pipservices3.components.auth.ICredentialStore
 * <p>
 * ### References ###
 * <ul>
 * <li>*:discovery:*:*:1.0     (optional) {@link IDiscovery} services to resolve connections
 * <li>*:credential-store:*:*:1.0   (optional) Credential stores to resolve credentials
 * </ul>
 * }
 */
public class CompositeConnectionResolver implements IReferenceable, IConfigurable {

    /**
     * The connection options
     */
    protected ConfigParams _options;

    /**
     * The connections resolver.
     */
    protected ConnectionResolver _connectionResolver = new ConnectionResolver();

    /**
     * The credentials resolver.
     */
    protected CredentialResolver _credentialResolver = new CredentialResolver();

    /**
     * The cluster support (multiple connections)
     */
    protected boolean _clusterSupported = true;

    /**
     * The default protocol
     */
    protected String _defaultProtocol = null;

    /**
     * The default port number
     */
    protected int _defaultPort = 0;

    /**
     * The list of supported protocols
     */
    List<String> _supportedProtocols = null;

    /**
     * Configures component by passing configuration parameters.
     *
     * @param configParams configuration parameters to be set.
     */
    @Override
    public void configure(ConfigParams configParams) throws ConfigException {
        this._connectionResolver.configure(configParams);
        this._credentialResolver.configure(configParams);
        this._options = configParams.getSection("options");
    }

    /**
     * Sets references to dependent components.
     *
     * @param references references to locate the component dependencies.
     */
    @Override
    public void setReferences(IReferences references) throws ReferenceException, ConfigException {
        this._connectionResolver.setReferences(references);
        this._credentialResolver.setReferences(references);
    }

    public ConfigParams resolve(String correlationId) throws ApplicationException {
        List<ConnectionParams> connections = this._connectionResolver.resolveAll(correlationId);
        connections = connections != null ? connections : new ArrayList<>();

        // Validate if cluster (multiple connections) is supported
        if (connections.size() > 0 && !this._clusterSupported) {
            throw new ConfigException(
                    correlationId,
                    "MULTIPLE_CONNECTIONS_NOT_SUPPORTED",
                    "Multiple (cluster) connections are not supported"
            );
        }

        for (ConnectionParams connection : connections)
            this.validateConnection(correlationId, connection);

        CredentialParams credential = _credentialResolver.lookup(correlationId);
        credential = credential != null ? credential : new CredentialParams();

        // Validate credential
        this.validateCredential(correlationId, credential);

        return this.composeOptions(connections, credential, this._options);
    }

    /**
     * Composes Composite connection options from connection and credential parameters.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param connections   connection parameters
     * @param credential    credential parameters
     * @param parameters    optional parameters
     * @return resolved options.
     */
    public ConfigParams compose(String correlationId, List<ConnectionParams> connections, CredentialParams credential, ConfigParams parameters) throws ConfigException {
        // Validate connection parameters
        for (ConnectionParams connection : connections) {
            this.validateConnection(correlationId, connection);
        }

        // Validate credential parameters
        this.validateCredential(correlationId, credential);

        // Compose final options
        return this.composeOptions(connections, credential, parameters);
    }

    protected void validateConnection(String correlationId, ConnectionParams connection) throws ConfigException {
        if (connection == null)
            throw new ConfigException(correlationId, "NO_CONNECTION", "Connection parameters are not set is not set");

        // URI usually contains all information
        String uri = connection.getUri();
        if (uri != null)
            return;

        String protocol = connection.getProtocolWithDefault(this._defaultProtocol);
        if (protocol == null) {
            throw new ConfigException(correlationId, "NO_PROTOCOL", "Connection protocol is not set");
        }
        if (this._supportedProtocols != null && !this._supportedProtocols.contains(protocol)) {
            throw new ConfigException(correlationId, "UNSUPPORTED_PROTOCOL", "The protocol " + protocol + " is not supported");
        }

        String host = connection.getHost();
        if (host == null) {
            throw new ConfigException(correlationId, "NO_HOST", "Connection host is not set");
        }

        int port = connection.getPortWithDefault(this._defaultPort);
        if (port == 0) {
            throw new ConfigException(correlationId, "NO_PORT", "Connection port is not set");
        }
    }

    /**
     * Validates credential parameters and throws an exception on error.
     * This method can be overriden in child classes.
     *
     * @param correlationId (optional) transaction id to trace execution through call chain.
     * @param credential    credential parameters to be validated
     */
    protected void validateCredential(String correlationId, CredentialParams credential) {
        // By default, the rules are open
    }

    /**
     * Composes connection and credential parameters into connection options.
     * This method can be overriden in child classes.
     *
     * @param connections a list of connection parameters
     * @param credential  credential parameters
     * @param parameters  optional parameters
     * @return a composed connection options.
     */
    private ConfigParams composeOptions(List<ConnectionParams> connections, CredentialParams credential, ConfigParams parameters) {
        // Connection options
        ConfigParams options = new ConfigParams();

        // Merge connection parameters
        for (ConnectionParams connection : connections)
            options = this.mergeConnection(options, connection);


        // Merge credential parameters
        options = this.mergeCredential(options, credential);

        // Merge optional parameters
        options = this.mergeOptional(options, parameters);

        // Perform final processing
        options = this.finalizeOptions(options);

        return options;
    }

    /**
     * Merges connection options with connection parameters
     * This method can be overriden in child classes.
     *
     * @param options    connection options
     * @param connection connection parameters to be merged
     * @return merged connection options.
     */
    protected ConfigParams mergeConnection(ConfigParams options, ConnectionParams connection) {
        return options.setDefaults(connection);
    }

    /**
     * Merges connection options with credential parameters
     * This method can be overriden in child classes.
     *
     * @param options    connection options
     * @param credential credential parameters to be merged
     * @return merged connection options.
     */
    protected ConfigParams mergeCredential(ConfigParams options, CredentialParams credential) {
        return options.override(credential);
    }

    /**
     * Merges connection options with optional parameters
     * This method can be overriden in child classes.
     *
     * @param options    connection options
     * @param parameters optional parameters to be merged
     * @return merged connection options.
     */
    protected ConfigParams mergeOptional(ConfigParams options, ConfigParams parameters) {
        return options.setDefaults(parameters);
    }

    /**
     * Finalize merged options
     * This method can be overriden in child classes.
     *
     * @param options connection options
     * @return finalized connection options
     */
    protected ConfigParams finalizeOptions(ConfigParams options) {
        return options;
    }
}

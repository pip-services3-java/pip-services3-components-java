package org.pipservices.components.connect;

import java.util.*;

import org.pipservices.commons.config.*;

public class MemoryDiscovery implements IDiscovery, IReconfigurable {
    private List<DiscoveryItem> _items = new ArrayList<DiscoveryItem>();
    private Object _lock = new Object();

    public MemoryDiscovery() { }

    public MemoryDiscovery(ConfigParams config) {
        if (config != null) configure(config);
    }

    private class DiscoveryItem {
        public String key;
        public ConnectionParams connection;
    }

    public void configure(ConfigParams config) {
        readConnections(config);
    }

    private void readConnections(ConfigParams connections) {
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

    public void register(String correlationId, String key, ConnectionParams connection) {
        synchronized (_lock) {
        	DiscoveryItem item = new DiscoveryItem();
        	item.key = key;
            item.connection = connection;
            _items.add(item);
        }
    }

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

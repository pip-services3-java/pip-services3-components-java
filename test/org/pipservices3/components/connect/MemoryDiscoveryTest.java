package org.pipservices3.components.connect;

import org.junit.Test;
import org.pipservices3.commons.config.ConfigParams;

import java.util.List;

import static org.junit.Assert.*;

public class MemoryDiscoveryTest {
    private final ConfigParams config = ConfigParams.fromTuples(
            "connections.key1.host", "10.1.1.100",
            "connections.key1.port", "8080",
            "connections.key2.host", "10.1.1.101",
            "connections.key2.port", "8082"
    );

    @Test
    public void testResolveConnections() {
        MemoryDiscovery discovery = new MemoryDiscovery();
        discovery.configure(config);

        // Resolve one
        ConnectionParams connection = discovery.resolveOne("123", "key1");

        assertEquals("10.1.1.100", connection.getHost());
        assertEquals(8080, connection.getPort());

        connection = discovery.resolveOne("123", "key2");

        assertEquals("10.1.1.101", connection.getHost());
        assertEquals(8082, connection.getPort());

        // Resolve all
        discovery.register(null, "key1",
                ConnectionParams.fromTuples("host", "10.3.3.151")
        );

        List<ConnectionParams> connections = discovery.resolveAll("123", "key1");

        assertTrue(connections.size() > 1);
    }
}

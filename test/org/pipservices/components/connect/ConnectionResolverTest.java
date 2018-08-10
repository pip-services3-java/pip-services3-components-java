package org.pipservices.components.connect;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;

public class ConnectionResolverTest {
	private static final ConfigParams RestConfig = ConfigParams.fromTuples(
        "connection.protocol", "http",
        "connection.host", "localhost",
        "connection.port", 3000
	);
	
	@Test
	public void testConfigure() {
		ConnectionResolver connectionResolver = new ConnectionResolver(RestConfig);
		List<ConnectionParams> configList = connectionResolver.getAll();
		assertEquals(configList.get(0).get("protocol"), "http");
		assertEquals(configList.get(0).get("host"), "localhost");
		assertEquals(configList.get(0).get("port"), "3000");
	}
	
	@Test
	public void testRegister() throws ApplicationException {
		ConnectionParams connectionParams = new ConnectionParams();
		ConnectionResolver connectionResolver = new ConnectionResolver(RestConfig);
		connectionResolver.register("correlationId", connectionParams);
		List<ConnectionParams> configList = connectionResolver.getAll();
		assertEquals(configList.size(), 1);

		connectionParams.setDiscoveryKey("Discovery key value");
		connectionResolver.register("correlationId", connectionParams);
		configList = connectionResolver.getAll();
		assertEquals(configList.size(), 1);
		
		IReferences references = new References();
		connectionResolver.setReferences(references);
		connectionResolver.register("correlationId", connectionParams);
		configList = connectionResolver.getAll();
		assertEquals(configList.size(), 2);
		assertEquals(configList.get(0).get("protocol"), "http");
		assertEquals(configList.get(0).get("host"), "localhost");
		assertEquals(configList.get(0).get("port"), "3000");
		assertEquals(configList.get(1).get("discovery_key"), "Discovery key value");
	}
	
	@Test
	public void testResolve() throws ApplicationException  {
		ConnectionResolver connectionResolver = new ConnectionResolver(RestConfig);
		ConnectionParams connectionParams = connectionResolver.resolve("correlationId");
		assertEquals(connectionParams.get("protocol"), "http");
		assertEquals(connectionParams.get("host"), "localhost");
		assertEquals(connectionParams.get("port"), "3000");
		
		ConfigParams RestConfigDiscovery = ConfigParams.fromTuples(
			"connection.protocol", "http",
		    "connection.host", "localhost",
		    "connection.port", 3000,
		    "connection.discovery_key", "Discovery key value"
		);
		IReferences references = new References();
		connectionResolver = new ConnectionResolver(RestConfigDiscovery , references);		
		try {
			connectionParams = connectionResolver.resolve("correlationId");
		} catch (ApplicationException e) {
			assertEquals("Discovery wasn't found to make resolution", e.getMessage());
		}
	}
}

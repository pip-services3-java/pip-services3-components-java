package org.pipservices3.components.connect;

import static org.junit.Assert.*;

import org.junit.*;
import org.pipservices3.components.connect.ConnectionParams;
import org.pipservices3.commons.errors.*;

public class ConnectionParamsTest {
	@Test
	public void testDiscovery() throws ApplicationException {
		ConnectionParams connection = new ConnectionParams();
		connection.setDiscoveryKey(null);
		assertNull(connection.getDiscoveryKey());
		
		connection.setDiscoveryKey("Discovery key value");
		assertEquals(connection.getDiscoveryKey(), "Discovery key value");
		assertTrue(connection.useDiscovery());
	}
	
	@Test
	public void testProtocol() throws ApplicationException {
		ConnectionParams connection = new ConnectionParams();
		connection.setProtocol(null);
		assertNull(connection.getProtocol());
		assertNull(connection.getProtocol(null));
		assertEquals(connection.getProtocol("https"), "https");
		
		connection.setProtocol("https");
		assertEquals(connection.getProtocol(), "https");
	}
	
	@Test
	public void testHost() throws ApplicationException {
		ConnectionParams connection = new ConnectionParams();
		assertNull(connection.getHost());
		connection.setHost(null);
		assertNull(connection.getHost());
		
		connection.setHost("localhost");
		assertEquals(connection.getHost(), "localhost");
	}
	
	@Test
	public void testPort() throws ApplicationException {
		ConnectionParams connection = new ConnectionParams();
		assertNull(connection.getHost());
		
		connection.setPort(8080);
		assertEquals(connection.getPort(), 8080);
	}
	
	@Test
	public void testUri() throws ApplicationException {
		ConnectionParams connection = new ConnectionParams();
		assertNull(connection.getUri());
		
		connection.setProtocol("https");
		connection.setPort(3000);
		connection.setHost("pipgoals");
		assertNull(connection.getUri());
	}

}

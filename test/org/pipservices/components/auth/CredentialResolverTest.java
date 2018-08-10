package org.pipservices.components.auth;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;

public class CredentialResolverTest {
	private static final ConfigParams RestConfig = ConfigParams.fromTuples(
        "credential.username", "Negrienko",
        "credential.password", "qwerty",
        "credential.access_key", "key",
        "credential.store_key", "store key"
	);
	
	@Test
	public void testConfigure() {
		CredentialResolver credentialResolver;
		credentialResolver = new CredentialResolver(RestConfig);
		List<CredentialParams> configList = credentialResolver.getAll();
		System.out.println(configList.get(0).get("username"));
		assertEquals(configList.get(0).get("username"), "Negrienko");
		assertEquals(configList.get(0).get("password"), "qwerty");
		assertEquals(configList.get(0).get("access_key"), "key");
		assertEquals(configList.get(0).get("store_key"), "store key");
	}
	
	@Test
	public void testLookup() throws ApplicationException {
		CredentialResolver credentialResolver;
		credentialResolver = new CredentialResolver();
		CredentialParams credential = credentialResolver.lookup("correlationId");
		assertNull(credential);
		
		ConfigParams RestConfigWithoutStoreKey = ConfigParams.fromTuples(
		        "credential.username", "Negrienko",
		        "credential.password", "qwerty",
		        "credential.access_key", "key"
		);
		credentialResolver = new CredentialResolver(RestConfigWithoutStoreKey);
		credential = credentialResolver.lookup("correlationId");
		assertEquals(credential.get("username"), "Negrienko");
		assertEquals(credential.get("password"), "qwerty");
		assertEquals(credential.get("access_key"), "key");
		assertNull(credential.get("store_key"));
		
		credentialResolver = new CredentialResolver(RestConfig);
		credential = credentialResolver.lookup("correlationId");
		assertNull(credential);
		
		credentialResolver.setReferences(new References());
		try {
			credential = credentialResolver.lookup("correlationId");
		} catch (ApplicationException e) {
			assertEquals("Credential store wasn't found to make lookup", e.getMessage());
		}
	}
}

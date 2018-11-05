package org.pipservices3.components.auth;

import static org.junit.Assert.*;

import org.junit.*;
import org.pipservices3.components.auth.CredentialParams;
import org.pipservices3.commons.errors.*;

public class CredentialParamsTest {
	@Test
	public void testStoreKey() throws ApplicationException {
		CredentialParams сredential = new CredentialParams();
		сredential.setStoreKey(null);
		assertNull(сredential.getStoreKey());
		
		сredential.setStoreKey("Store key");
		assertEquals(сredential.getStoreKey(), "Store key");
		assertTrue(сredential.useCredentialStore());
	}
	
	@Test
	public void testUsername() throws ApplicationException {
		CredentialParams сredential = new CredentialParams();
		сredential.setUsername(null);
		assertNull(сredential.getUsername());
		
		сredential.setUsername("Kate Negrienko");
		assertEquals(сredential.getUsername(), "Kate Negrienko");
	}
	
	@Test
	public void testPassword() throws ApplicationException {
		CredentialParams сredential = new CredentialParams();
		сredential.setPassword(null);
		assertNull(сredential.getPassword());
		
		сredential.setPassword("qwerty");
		assertEquals(сredential.getPassword(), "qwerty");
	}
	
	@Test
	public void testAccessKey() throws ApplicationException {
		CredentialParams сredential = new CredentialParams();
		сredential.setAccessKey(null);
		assertNull(сredential.getAccessKey());
		
		сredential.setAccessKey("key");
		assertEquals(сredential.getAccessKey(), "key");
	}

}

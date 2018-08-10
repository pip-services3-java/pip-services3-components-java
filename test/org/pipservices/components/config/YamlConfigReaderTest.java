package org.pipservices.components.config;

import static org.junit.Assert.*;

import org.junit.*;
import org.pipservices.commons.config.ConfigParams;

public class YamlConfigReaderTest {

	@Test
	public void testReadConfig() throws Exception {
		ConfigParams config = YamlConfigReader.readConfig(null, "data/config.yaml");
		
		assertEquals(7, config.size());
		assertEquals(123, config.getAsInteger("field1.field11"));
		assertEquals("ABC", config.getAsString("field1.field12"));
		assertEquals(123, config.getAsInteger("field2.0"));
		assertEquals("ABC", config.getAsString("field2.1"));
		assertEquals(543, config.getAsInteger("field2.2.field21"));
		assertEquals("XYZ", config.getAsString("field2.2.field22"));
		assertEquals(true, config.getAsBoolean("field3"));
	}

}

package org.pipservices.components.config;

import static org.junit.Assert.*;

import org.junit.*;
import org.pipservices.commons.config.*;

public class NameResolverTest {

	@Test
	public void testNormalNameResolution() {
        ConfigParams config = ConfigParams.fromTuples("Id", "ABC");
        String name = NameResolver.resolve(config);
        assertEquals("ABC", name);

        config = ConfigParams.fromTuples("NAME", "ABC");
        name = NameResolver.resolve(config);
        assertEquals("ABC", name);
    }

    @Test
    public void testEmptyName() {
        ConfigParams config = ConfigParams.fromTuples();
        String name = NameResolver.resolve(config);
        assertNull(name);
    }

}

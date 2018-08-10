package org.pipservices.components.info;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import org.pipservices.commons.config.ConfigParams;

public final class ContextInfoTest {

	@Test
	public void testName()
    {
		ContextInfo contextInfo = new ContextInfo();

        assertEquals(contextInfo.getName(), "unknown");

        contextInfo.setName("new name");

        assertEquals(contextInfo.getName(), "new name");
    }
	
	@Test
	public void testDescription()
    {
		ContextInfo contextInfo = new ContextInfo();
		assertNull(contextInfo.getDescription());

        contextInfo.setDescription("new description");
        assertEquals(contextInfo.getDescription(), "new description");
    }
	
	@Test
	public void TestContextId()
    {
		ContextInfo contextInfo = new ContextInfo();

        contextInfo.setContextId("new context id");

        assertEquals("new context id", contextInfo.getContextId());
    }
	
	@Test
	public void TestStartTime()
    {
		ContextInfo contextInfo = new ContextInfo();
		contextInfo.setStartTime(ZonedDateTime.of(2015, 10, 18, 0, 30, 0, 0, 
						    ZoneId.of("America/Sao_Paulo")));
        assertEquals(contextInfo.getStartTime().getYear(), ZonedDateTime.now().getYear());
        assertEquals(contextInfo.getStartTime().getMonth(), ZonedDateTime.now().getMonth());
        
        contextInfo.setStartTime(ZonedDateTime.of(1975, 4, 8, 0, 0, 0, 0, ZoneId.of("Russia/Moscow") ));
        assertEquals(contextInfo.getStartTime(), ZonedDateTime.of(1975, 4, 8, 0, 0, 0, 0, ZoneId.of("Russia/Moscow")));
    }
	
	@Test
	public void TestFromConfigs()
    {
		ConfigParams config = ConfigParams.fromTuples(
             "info.name", "new name",
             "info.description", "new description",
             "properties.access_key", "key",
             "properties.store_key", "store key"
             );

		ContextInfo contextInfo = ContextInfo.fromConfig(config);
         assertEquals(contextInfo.getName(), "new name");
         assertEquals(contextInfo.getDescription(), "new description");
     }
}

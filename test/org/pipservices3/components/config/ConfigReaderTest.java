package org.pipservices3.components.config;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.run.Parameters;

public class ConfigReaderTest {
    
	@Test
    public void TestParameterize() throws Exception
    {
        String config = "{{#if A}}{{B}}{{/if}}";
        Map<String, Object> values = Parameters.fromTuples(
        		"A", "true",
        		"B", "XYZ"
    		);
        
        ConfigParams parameters = new ConfigParams();
        parameters.append(values);
      
        assertEquals("XYZ", ConfigReader.parameterize(config, parameters));
    }
}

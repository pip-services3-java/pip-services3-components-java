package org.pipservices.components.config;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import org.pipservices.commons.config.ConfigParams;
import org.pipservices.commons.run.Parameters;

public class ConfigReaderTest {
    
	@Test
    public void TestParameterize() throws IOException
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

package org.pipservices.components.config;

import java.io.*;

import org.pipservices.commons.config.ConfigParams;
import org.pipservices.commons.errors.*;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.*;

public class YamlConfigReader extends FileConfigReader {
	private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
	private static TypeReference<Object> typeRef = new TypeReference<Object>() {};

	public YamlConfigReader() {}
	
	public YamlConfigReader(String path) {
		super(path);
	}
		
    public Object readObject(String correlationId) throws ApplicationException {
    	if (_path == null)
        	throw new ConfigException(correlationId, "NO_PATH", "Missing config file path");
    	
        try {
            File file = new File(_path); 
        	return yamlMapper.readValue(file, typeRef);
        } catch (Exception ex) {
        	throw new FileException(
    			correlationId, 
    			"READ_FAILED", 
    			"Failed reading configuration " + _path + ": " + ex
			)
        	.withDetails("path", _path)
    		.withCause(ex);
        }
    }

    @Override
    public ConfigParams performReadConfig(String correlationId) throws ApplicationException {
    	Object value = readObject(correlationId);
    	return ConfigParams.fromValue(value);
    }
        
    public static Object readObject(String correlationId, String path) throws ApplicationException {
        return new YamlConfigReader(path).readObject(correlationId);
    }

    public static ConfigParams readConfig(String correlationId, String path) throws ApplicationException {
        return new YamlConfigReader(path).readConfig(correlationId);
    }
}

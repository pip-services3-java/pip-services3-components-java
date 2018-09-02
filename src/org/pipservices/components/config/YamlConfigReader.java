package org.pipservices.components.config;

import java.nio.file.*;

import org.pipservices.commons.config.*;
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
		
    public Object readObject(String correlationId, ConfigParams parameters) throws ApplicationException {
    	if (_path == null)
        	throw new ConfigException(correlationId, "NO_PATH", "Missing config file path");
    	
        try {
            Path path = Paths.get(_path); 
            
            String yaml = new String(Files.readAllBytes(path));
            yaml = parameterize(yaml, parameters);
            
            return yamlMapper.readValue(yaml, typeRef);
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
    public ConfigParams readConfig(String correlationId, ConfigParams parameters) throws ApplicationException {
    	Object value = readObject(correlationId, parameters);
    	return ConfigParams.fromValue(value);
    }
        
    public static Object readObject(String correlationId, String path, ConfigParams parameters) throws ApplicationException {
        return new YamlConfigReader(path).readObject(correlationId, parameters);
    }

    public static ConfigParams readConfig(String correlationId, String path, ConfigParams parameters) throws ApplicationException {
        return new YamlConfigReader(path).readConfig(correlationId, parameters);
    }
}

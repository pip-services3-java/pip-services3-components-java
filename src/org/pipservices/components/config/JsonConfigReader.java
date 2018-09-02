package org.pipservices.components.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.pipservices.commons.config.ConfigParams;
import org.pipservices.commons.errors.*;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

public class JsonConfigReader extends FileConfigReader {
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static TypeReference<Object> typeRef = new TypeReference<Object>() {};

	public JsonConfigReader() {}
	
	public JsonConfigReader(String path) {
		super(path);
	}
			
    public Object readObject(String correlationId, ConfigParams parameters) throws ApplicationException {
    	if (_path == null)
        	throw new ConfigException(correlationId, "NO_PATH", "Missing config file path");
    	
        try {
            Path path = Paths.get(_path); 
            
            String json = new String(Files.readAllBytes(path));
            json = parameterize(json, parameters);
            
        	return jsonMapper.readValue(json, typeRef);
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
        return new JsonConfigReader(path).readObject(correlationId, parameters);
    }

    public static ConfigParams readConfig(String correlationId, String path, ConfigParams parameters) throws ApplicationException {
        return new JsonConfigReader(path).readConfig(correlationId, parameters);
    }
}

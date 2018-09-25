package org.pipservices.components.config;

import java.nio.file.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.*;

/**
 * Config reader that reads configuration from YAML file.
 * 
 * The reader supports parameterization using Handlebar template engine.
 * 
 * ### Configuration parameters ###
 * 
 * path:          path to configuration file
 * parameters:    this entire section is used as template parameters
 *   ...
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ======== config.yml ======
 * key1: "{{KEY1_VALUE}}"
 * key2: "{{KEY2_VALUE}}"
 * ===========================
 * 
 * YamlConfigReader configReader = new YamlConfigReader("config.yml");
 * 
 * ConfigParams parameters = ConfigParams.fromTuples("KEY1_VALUE", 123, "KEY2_VALUE", "ABC");
 * configReader.readConfig("123", parameters, (err, config) => {
 *      // Result: key1=123;key2=ABC
 * });
 * }
 * </pre>
 */
public class YamlConfigReader extends FileConfigReader {
	private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
	private static TypeReference<Object> typeRef = new TypeReference<Object>() {
	};

	/**
	 * Creates a new instance of the config reader.
	 */
	public YamlConfigReader() {
	}

	/**
	 * Creates a new instance of the config reader.
	 * 
	 * @param path (optional) a path to configuration file.
	 */
	public YamlConfigReader(String path) {
		super(path);
	}

	/**
	 * Reads configuration file, parameterizes its content and converts it into JSON
	 * object.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param parameters    values to parameters the configuration.
	 * @return a JSON object with configuration.
	 * @throws ApplicationException when error occured.
	 */
	public Object readObject(String correlationId, ConfigParams parameters) throws ApplicationException {
		if (_path == null)
			throw new ConfigException(correlationId, "NO_PATH", "Missing config file path");

		try {
			Path path = Paths.get(_path);

			String yaml = new String(Files.readAllBytes(path));
			yaml = parameterize(yaml, parameters);

			return yamlMapper.readValue(yaml, typeRef);
		} catch (Exception ex) {
			throw new FileException(correlationId, "READ_FAILED", "Failed reading configuration " + _path + ": " + ex)
					.withDetails("path", _path).withCause(ex);
		}
	}

	/**
	 * Reads configuration and parameterize it with given values.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param parameters    values to parameters the configuration or null to skip
	 *                      parameterization.
	 * @return ConfigParams configuration.
	 * @throws ApplicationException when error occured.
	 */
	@Override
	public ConfigParams readConfig(String correlationId, ConfigParams parameters) throws ApplicationException {
		Object value = readObject(correlationId, parameters);
		return ConfigParams.fromValue(value);
	}

	/**
	 * Reads configuration file, parameterizes its content and converts it into JSON
	 * object.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param path          a path to configuration file.
	 * @param parameters    values to parameters the configuration.
	 * @return a JSON object with configuration.
	 * @throws ApplicationException when error occured.
	 */
	public static Object readObject(String correlationId, String path, ConfigParams parameters)
			throws ApplicationException {
		return new YamlConfigReader(path).readObject(correlationId, parameters);
	}

	/**
	 * Reads configuration from a file, parameterize it with given values and
	 * returns a new ConfigParams object.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param path          a path to configuration file.
	 * @param parameters    values to parameters the configuration or null to skip
	 *                      parameterization.
	 * @return ConfigParams configuration.
	 * @throws ApplicationException when error occured.
	 */
	public static ConfigParams readConfig(String correlationId, String path, ConfigParams parameters)
			throws ApplicationException {
		return new YamlConfigReader(path).readConfig(correlationId, parameters);
	}
}

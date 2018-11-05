package org.pipservices3.components.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.errors.*;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

/**
 * Config reader that reads configuration from JSON file.
 * <p>
 * The reader supports parameterization using Handlebars template engine.
 * <p>
 * ### Configuration parameters ###
 * <ul>
 * <li>path:          path to configuration file
 * <li>parameters:    this entire section is used as template parameters
 * <li>...
 * </ul>
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ======== config.json ======
 * { "key1": "{{KEY1_VALUE}}", "key2": "{{KEY2_VALUE}}" }
 * ===========================
 * 
 * JsonConfigReader configReader = new JsonConfigReader("config.json");
 * 
 * ConfigParams parameters = ConfigParams.fromTuples("KEY1_VALUE", 123, "KEY2_VALUE", "ABC");
 * configReader.readConfig("123", parameters);
 * }
 * </pre>
 * @see IConfigReader
 * @see FileConfigReader
 */
public class JsonConfigReader extends FileConfigReader {
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static TypeReference<Object> typeRef = new TypeReference<Object>() {
	};

	/**
	 * Creates a new instance of the config reader.
	 */
	public JsonConfigReader() {
	}

	/**
	 * Creates a new instance of the config reader.
	 * 
	 * @param path (optional) a path to configuration file.
	 */
	public JsonConfigReader(String path) {
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

			String json = new String(Files.readAllBytes(path));
			json = parameterize(json, parameters);

			return jsonMapper.readValue(json, typeRef);
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
	 * @param parameters    values to parameters the configuration
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
		return new JsonConfigReader(path).readObject(correlationId, parameters);
	}

	/**
	 * Reads configuration from a file, parameterize it with given values and
	 * returns a new ConfigParams object.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param path          a path to configuration file.
	 * @param parameters    values to parameters the configuration.
	 * @return ConfigParams configuration.
	 * @throws ApplicationException when error occured.
	 */
	public static ConfigParams readConfig(String correlationId, String path, ConfigParams parameters)
			throws ApplicationException {
		return new JsonConfigReader(path).readConfig(correlationId, parameters);
	}
}

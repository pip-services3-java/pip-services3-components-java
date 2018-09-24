package org.pipservices.components.config;

import java.io.IOException;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.ApplicationException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

/**
 * Abstract config reader that supports configuration parameterization.
 * 
 * ### Configuration parameters ###
 * 
 * parameters:            this entire section is used as template parameters
 *   ...
 * 
 *  @see IConfigReader
 */
public abstract class ConfigReader implements IConfigurable, IConfigReader {
	private ConfigParams _parameters = new ConfigParams();

	/**
	 * Configures component by passing configuration parameters.
	 * 
	 * @param config configuration parameters to be set.
	 */
	public void configure(ConfigParams config) {
		ConfigParams parameters = config.getSection("parameters");
		if (parameters.size() > 0) {
			_parameters = parameters;
		}
	}

	/**
	 * Reads configuration and parameterize it with given values.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @param parameters    values to parameters the configuration or null to skip
	 *                      parameterization.
	 * @return receives ConfigParams configuration.
	 * @throws ApplicationException when error occured.
	 */
	public abstract ConfigParams readConfig(String correlationId, ConfigParams parameters) throws ApplicationException;

	/**
	 * Parameterized configuration template given as string with dynamic parameters.
	 * 
	 * The method uses Handlebars template engine.
	 * 
	 * @param config     a string with configuration template to be parameterized
	 * @param parameters dynamic parameters to inject into the template
	 * @return a parameterized configuration string.
	 * @throws IOException when input/output error occured.
	 */
	protected static String parameterize(String config, ConfigParams parameters) throws IOException {
		if (parameters == null) {
			return config;
		}

		Handlebars handlebars = new Handlebars();
		Template template = handlebars.compileInline(config);

		return template.apply(parameters);
	}
}
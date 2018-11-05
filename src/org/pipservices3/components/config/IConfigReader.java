package org.pipservices3.components.config;

import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.errors.*;

/**
 * Interface for configuration readers that retrieve configuration from various sources
 * and make it available for other components.
 * <p>
 * Some IConfigReader implementations may support configuration parameterization.
 * The parameterization allows to use configuration as a template and inject there dynamic values.
 * The values may come from application command like arguments or environment variables.
 */
public interface IConfigReader {
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
	ConfigParams readConfig(String correlationId, ConfigParams parameters) throws ApplicationException;
}

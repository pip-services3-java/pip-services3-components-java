package org.pipservices.components.config;

import org.pipservices.commons.config.ConfigParams;
import org.pipservices.commons.errors.*;

public interface IConfigReader {
	ConfigParams readConfig(String correlationId, ConfigParams parameters) throws ApplicationException;
}

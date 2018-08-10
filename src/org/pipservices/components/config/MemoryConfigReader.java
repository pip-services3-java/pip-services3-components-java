package org.pipservices.components.config;

import org.pipservices.commons.config.*;

public class MemoryConfigReader implements IConfigReader, IReconfigurable {
    protected ConfigParams _config = new ConfigParams();

    public MemoryConfigReader() {}

    public MemoryConfigReader(ConfigParams config) {
        _config = config != null ? config : new ConfigParams();
    }

    public void configure(ConfigParams config) {
        _config = config;
    }

    public ConfigParams readConfig(String correlationId) {
        return new ConfigParams(_config);
    }

    public ConfigParams readConfigSection(String correlationId, String section) {
        return _config != null ? _config.getSection(section) : null;
    }
}

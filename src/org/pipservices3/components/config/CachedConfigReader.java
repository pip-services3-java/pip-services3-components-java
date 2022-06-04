package org.pipservices3.components.config;

import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IReconfigurable;
import org.pipservices3.commons.errors.ApplicationException;

public abstract class CachedConfigReader implements IConfigReader, IReconfigurable {
    private long _lastRead = 0;
    private ConfigParams _config;
    private long _timeout = 60000;

    public CachedConfigReader() {}
        
    public long getTimeout() { return _timeout; }
    public void setTimeout(long value) { _timeout = value; }

    public void configure(ConfigParams config) {
        _timeout = config.getAsLongWithDefault("timeout", _timeout);
    }

    protected abstract ConfigParams performReadConfig(String correlationId, ConfigParams parameters) throws ApplicationException;

    public ConfigParams readConfig(String correlationId, ConfigParams parameters) throws ApplicationException {
        if (_config != null && System.currentTimeMillis() < _lastRead + _timeout)
            return _config;

        _config = performReadConfig(correlationId, parameters);
        _lastRead = System.currentTimeMillis();

        return _config;
    }

    public ConfigParams readConfigSection(String correlationId, ConfigParams parameters, String section) throws ApplicationException {
        ConfigParams config = readConfig(correlationId, parameters);
        return config != null ? config.getSection(section) : null;
    }
}

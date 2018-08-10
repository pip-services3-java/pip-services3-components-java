package org.pipservices.components.config;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.ApplicationException;

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

    protected abstract ConfigParams performReadConfig(String correlationId) throws ApplicationException;

    public ConfigParams readConfig(String correlationId) throws ApplicationException {
        if (_config != null && System.currentTimeMillis() < _lastRead + _timeout)
            return _config;

        _config = performReadConfig(correlationId);
        _lastRead = System.currentTimeMillis();

        return _config;
    }

    public ConfigParams readConfigSection(String correlationId, String section) throws ApplicationException {
        ConfigParams config = readConfig(correlationId);
        return config != null ? config.getSection(section) : null;
    }
}

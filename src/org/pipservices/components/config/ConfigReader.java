package org.pipservices.components.config;

import java.io.IOException;

import org.pipservices.commons.config.*;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

/// <summary>
/// Config reader with parameters
/// </summary>
/// <seealso cref="PipServices.Commons.Config.IConfigurable" />

public abstract class ConfigReader implements IConfigurable, IConfigReader
{
    private ConfigParams _parameters = new ConfigParams();

    /// <summary>
    /// Sets the components configuration.
    /// </summary>
    /// <param name="config">Configuration parameters.</param>
    
    public void configure(ConfigParams config)
    {
    	ConfigParams parameters = config.getSection("parameters");
        if (parameters.size() > 0)
        {
            _parameters = parameters;
        }
    }

    /// <summary>
    /// Reads the configuration.
    /// </summary>
    /// <param name="correlationId">The correlation identifier.</param>
    /// <param name="parameters">The parameters.</param>
    
    public abstract ConfigParams readConfig(String correlationId, ConfigParams parameters);

    /// <summary>
    /// Parameterizes the specified configuration.
    /// </summary>
    /// <param name="config">The configuration.</param>
    /// <param name="parameters">The parameters.</param>
    
    protected static String parameterize(String config, ConfigParams parameters) throws IOException
    {
        if (parameters == null)
        {
            return config;
        }
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline(config);

        return template.apply(parameters);
    }
}
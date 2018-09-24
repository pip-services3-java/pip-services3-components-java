package org.pipservices.components;

import org.pipservices.commons.config.*;
import org.pipservices.components.count.CompositeCounters;
import org.pipservices.commons.errors.ConfigException;
import org.pipservices.components.log.CompositeLogger;
import org.pipservices.commons.refer.*;

/**
 * Abstract component that supportes configurable dependencies, logging
 * and performance counters.
 * 
 * ### Configuration parameters ###
 * 
 * dependencies:
 *   [dependency name 1]: Dependency 1 locator (descriptor)
 *   ...
 *   [dependency name N]: Dependency N locator (descriptor)
 * 
 * ### References ###
 * 
 * - *:counters:*:*:1.0       (optional) [[ICounters]] components to pass collected measurements
 * - *:logger:*:*:1.0         (optional) [[ILogger]] components to pass log messages
 * - ...                      References must match configured dependencies.
 */
public class Component implements IConfigurable, IReferenceable {
	
	protected DependencyResolver _dependencyResolver = new DependencyResolver();
    protected CompositeLogger _logger = new CompositeLogger();
    protected CompositeCounters _counters = new CompositeCounters();

    /**
     * Configures component by passing configuration parameters.
     * 
     * @param config    configuration parameters to be set.
     */
	public void configure(ConfigParams config) throws ConfigException {
		_dependencyResolver.configure(config);
        _logger.configure(config);	
	}
    
	/**
	 * Sets references to dependent components.
	 * 
	 * @param references 	references to locate the component dependencies. 
     */
	public void setReferences(IReferences references) throws ReferenceException {
		_dependencyResolver.setReferences(references);
        _logger.setReferences(references);
        _counters.setReferences(references);	
	}

	

}

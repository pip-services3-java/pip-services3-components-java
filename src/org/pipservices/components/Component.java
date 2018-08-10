package org.pipservices.components;

import org.pipservices.commons.config.*;
import org.pipservices.components.count.CompositeCounters;
import org.pipservices.commons.errors.ConfigException;
import org.pipservices.components.log.CompositeLogger;
import org.pipservices.commons.refer.*;

public class Component implements IConfigurable, IReferenceable {
	
	protected DependencyResolver _dependencyResolver = new DependencyResolver();
    protected CompositeLogger _logger = new CompositeLogger();
    protected CompositeCounters _counters = new CompositeCounters();

	public void setReferences(IReferences references) throws ReferenceException {
		_dependencyResolver.setReferences(references);
        _logger.setReferences(references);
        _counters.setReferences(references);	
	}

	public void configure(ConfigParams config) throws ConfigException {
		_dependencyResolver.configure(config);
        _logger.configure(config);	
	}

}

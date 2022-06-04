package org.pipservices3.components;

import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IConfigurable;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.DependencyResolver;
import org.pipservices3.commons.refer.IReferenceable;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.components.count.CompositeCounters;
import org.pipservices3.components.log.CompositeLogger;
import org.pipservices3.components.trace.CompositeTracer;

/**
 * Abstract component that supportes configurable dependencies, logging
 * and performance counters.
 * <p>
 * ### Configuration parameters ###
 * <ul>
 * <li>dependencies:
 *   <ul>
 *   <li>[dependency name 1]: Dependency 1 locator (descriptor)
 *   <li>...
 *   <li>[dependency name N]: Dependency N locator (descriptor)
 * 	 </ul>
 * </ul>
 * <p>
 * ### References ###
 * <ul>
 * <li>*:counters:*:*:1.0       (optional) {@link org.pipservices3.components.count.ICounters} components to pass collected measurements
 * <li>*:logger:*:*:1.0         (optional) {@link org.pipservices3.components.log.ILogger} components to pass log messages
 * <li>*:tracer:*:*:1.0			(optional) {@link org.pipservices3.components.trace.ITracer} components to trace executed operations
 * <li>...                      References must match configured dependencies.
 * </ul>
 */
public class Component implements IConfigurable, IReferenceable {

    protected DependencyResolver _dependencyResolver = new DependencyResolver();
    protected CompositeLogger _logger = new CompositeLogger();
    protected CompositeCounters _counters = new CompositeCounters();
    protected CompositeTracer _tracer = new CompositeTracer();

    /**
     * Configures component by passing configuration parameters.
     *
     * @param config configuration parameters to be set.
     */
    public void configure(ConfigParams config) throws ConfigException {
        _dependencyResolver.configure(config);
        _logger.configure(config);
    }

    /**
     * Sets references to dependent components.
     *
     * @param references references to locate the component dependencies.
     */
    public void setReferences(IReferences references) throws ReferenceException {
        _dependencyResolver.setReferences(references);
        _logger.setReferences(references);
        _counters.setReferences(references);
        _tracer.setReferences(references);
    }
}

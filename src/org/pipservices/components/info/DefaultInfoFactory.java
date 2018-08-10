package org.pipservices.components.info;

import org.pipservices.components.build.Factory;
import org.pipservices.commons.refer.*;

public class DefaultInfoFactory extends Factory{
	
	public final static Descriptor Descriptor = new Descriptor("pip-services", "factory", "info", "default", "1.0");
    public final static Descriptor ContextInfoDescriptor = new Descriptor("pip-services", "context-info", "default", "*", "1.0");
    public final static Descriptor ContainerInfoDescriptor = new Descriptor("pip-services", "container-info", "default", "*", "1.0");
        
    public DefaultInfoFactory(){
        registerAsType(ContextInfoDescriptor, ContextInfo.class);
        registerAsType(ContainerInfoDescriptor, ContextInfo.class);
    }

}

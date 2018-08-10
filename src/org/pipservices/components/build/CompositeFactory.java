package org.pipservices.components.build;

import java.util.*;

public class CompositeFactory implements IFactory {
	private List<IFactory> _factories = new ArrayList<IFactory>();
	
	public CompositeFactory() {}
	
	public CompositeFactory(IFactory... factories) {
		if (factories != null) {
			for (IFactory factory : factories)
				_factories.add(factory);
		}
	}
	
	public void add(IFactory factory) {
		if (factory == null)
			throw new NullPointerException("Factory cannot be null");
		
		_factories.add(factory);
	}
	
	public void remove(IFactory factory) {
		_factories.remove(factory);
	}
	
	public Object canCreate(Object locator) {
		if (locator == null)
			throw new NullPointerException("Locator cannot be null");
		
		// Iterate from the latest factories
		for (int index = _factories.size() - 1; index >= 0; index--) {
			Object thisLocator = _factories.get(index).canCreate(locator);
			if (thisLocator != null)
				return thisLocator;
		}
		
		return null;
	}
	
	public Object create(Object locator) throws CreateException {
		if (locator == null)
			throw new NullPointerException("Locator cannot be null");
		
		// Iterate from the latest factories
		for (int index = _factories.size() - 1; index >= 0; index--) {
			IFactory factory = _factories.get(index);
			if (factory.canCreate(locator) != null)
				return factory.create(locator);
		}
		
		throw new CreateException(null, locator);
	}
	
}

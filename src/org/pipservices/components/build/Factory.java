package org.pipservices.components.build;

import java.util.*;

import org.pipservices.commons.reflect.*;

public class Factory implements IFactory {
	
	public interface IComponentFactory {
		Object create(Object locator) throws Exception;
	}
	
	private class DefaultComponentFactory implements IComponentFactory {
		private Class<?> _type;
		
		public DefaultComponentFactory(Class<?> type) {
			_type = type;
		}
		
		public Object create(Object locator) throws Exception {
			return TypeReflector.createInstanceByType(_type);
		}
	}

	private class Registration {
		private Object _locator;
		private IComponentFactory _factory;

		public Registration(Object locator, IComponentFactory factory) {
			this._locator = locator;
			this._factory = factory;
		}

		public Object getLocator() { return _locator; }
		public IComponentFactory getFactory() { return _factory; }
	}

	private List<Registration> _registrations = new ArrayList<Registration>();
	
	public void register(Object locator, IComponentFactory factory) {
		if (locator == null)
			throw new NullPointerException("Locator cannot be null");
		if (factory == null)
			throw new NullPointerException("Factory cannot be null");
		
		_registrations.add(new Registration(locator, factory));
	}

	public void registerAsType(Object locator, Class<?> type) {
		if (locator == null)
			throw new NullPointerException("Locator cannot be null");
		if (type == null)
			throw new NullPointerException("Type cannot be null");
		
		IComponentFactory factory = new DefaultComponentFactory(type);
		_registrations.add(new Registration(locator, factory));
	}
	
	@Override
	public Object canCreate(Object locator) {
		for (Registration registration : _registrations) {
			Object thisLocator = registration.getLocator();
			if (thisLocator.equals(locator))
				return thisLocator;
		}
		return null;
	}

	@Override
	public Object create(Object locator) throws CreateException {
		for (Registration registration : _registrations) {
			if (registration.getLocator().equals(locator)) {
				try {
					return registration.getFactory().create(locator);
				} catch (Exception ex) {
					if (ex instanceof CreateException)
						throw (CreateException)ex;
					
					throw (CreateException) new CreateException(
						null, 
						"Failed to create object for " + locator
					).withCause(ex);
				}
			}
		}
		return null;
	}
	
}

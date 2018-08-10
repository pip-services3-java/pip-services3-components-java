package org.pipservices.components.build;

/**
 * Interface for component factories
 */
public interface IFactory {
	/**
	 * Checks if factory is able to create requested component and returns its locator.
	 * @param locator a locator for requested component
	 * @return a locator of object to be created
	 */
	Object canCreate(Object locator);
	
	/**
	 * Creates a component by requested locator
	 * @param locator a requested components
	 * @return an instance of created component
	 * @throws CreateException when component creation fails 
	 */
	Object create(Object locator) throws CreateException;
}

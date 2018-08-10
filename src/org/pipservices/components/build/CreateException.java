package org.pipservices.components.build;

import org.pipservices.commons.errors.*;

/**
 * Exception thrown when component cannot be created by a factory
 */
public class CreateException extends InternalException {
	private static final long serialVersionUID = 2506495188126378894L;

	public CreateException() {
		this(null, (String)null);
	}

	public CreateException(String correlationId, Object locator) {
		super(correlationId, "CANNOT_CREATE", "Requested component " + locator + " cannot be created");
		this.withDetails("locator", locator);
	}

	public CreateException(String correlationId, String message) {
		super(correlationId, "CANNOT_CREATE", message);
	}
}

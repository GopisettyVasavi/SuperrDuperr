package com.superrduperr.todo.exception;
/**
 * Exception to be used when a resource is not found.
 * @author Vasavi
 *
 */
public class ResourceNotFoundException extends Exception {

	private static final long serialVersionUID = -9079454849611061074L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(final String message) {
		super(message);
	}

}

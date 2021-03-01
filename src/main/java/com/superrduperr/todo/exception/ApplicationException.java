package com.superrduperr.todo.exception;

/**
 * Application Exception class to be used across the application whenever a
 * custom exception need to be thrown.
 * 
 * @author Vasavi
 *
 */
public class ApplicationException extends Exception {

	private static final long serialVersionUID = -9079449611061074L;

	public ApplicationException() {
		super();
	}

	public ApplicationException(final String message) {
		super(message);
	}

}

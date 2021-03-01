package com.superrduperr.todo.exception;
/**
 * Exception to be used across the application when the given input is invalid.
 * @author Vasavi
 *
 */
public class InvalidInputException extends Exception {

	private static final long serialVersionUID = -9079454811061074L;

	public InvalidInputException() {
		super();
	}

	public InvalidInputException(final String message) {
		super(message);
	}

}


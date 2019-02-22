package com.binarskugga.skuggahttps.api.exception;

public class CannotMapFieldException extends RuntimeException {

	public CannotMapFieldException(String message) {
		super(message);
	}

	public CannotMapFieldException() {
		super("There was an error in the mapping of this entity.");
	}

}

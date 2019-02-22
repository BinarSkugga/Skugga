package com.binarskugga.skuggahttps.api.exception;

public class InvalidArgumentCountException extends RuntimeException {

	public InvalidArgumentCountException(String message) {
		super(message);
	}

	public InvalidArgumentCountException() {
		super("Too much argument are specified.");
	}

}

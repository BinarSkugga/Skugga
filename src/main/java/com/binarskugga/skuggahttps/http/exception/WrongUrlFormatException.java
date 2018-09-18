package com.binarskugga.skuggahttps.http.exception;

public class WrongUrlFormatException extends RuntimeException {

	public WrongUrlFormatException() {
		super();
	}

	public WrongUrlFormatException(String message) {
		super(message);
	}
}
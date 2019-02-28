package com.binarskugga.skugga.api.exception;

import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class InvalidBodyException extends BadRequestException {

	public InvalidBodyException(String message) {
		super(message);
	}

	public InvalidBodyException() {
		super("The body provided is in the wrong format.");
	}

}

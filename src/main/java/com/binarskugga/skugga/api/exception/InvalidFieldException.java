package com.binarskugga.skugga.api.exception;

import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class InvalidFieldException extends BadRequestException {

	public InvalidFieldException(String message) {
		super(message);
	}

	public InvalidFieldException() {
		super("One or more of the fields passed are invalid or using the wrong format.");
	}

}

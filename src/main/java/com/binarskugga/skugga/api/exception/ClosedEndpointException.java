package com.binarskugga.skugga.api.exception;

import com.binarskugga.skugga.api.exception.http.BadRequestException;

public class ClosedEndpointException extends BadRequestException {

	public ClosedEndpointException(String message) {
		super(message);
	}

	public ClosedEndpointException() {
		super("This endpoint has been closed and cannot be accessed.");
	}

}

package com.binarskugga.skuggahttps.exception;

import static com.binarskugga.skuggahttps.Response.*;

public class BadRequestException extends HTTPException {

	public BadRequestException(String message) {
		super(BAD_REQUEST, message);
	}

	public BadRequestException() {
		super(BAD_REQUEST);
	}

}

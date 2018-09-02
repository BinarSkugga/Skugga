package com.binarskugga.skuggahttps.http.exception;

import static com.binarskugga.skuggahttps.http.Response.*;

public class BadRequestException extends HTTPException {

	public BadRequestException(String message) {
		super(BAD_REQUEST, message);
	}

	public BadRequestException() {
		super(BAD_REQUEST);
	}
}

package com.binarskugga.skuggahttps.http.exception;

import static com.binarskugga.skuggahttps.http.Response.*;

public class InvalidTargetException extends HTTPException {

	public InvalidTargetException(String message) {
		super(INTERNAL_ERROR, message);
	}

	public InvalidTargetException() {
		super(INTERNAL_ERROR);
	}
}

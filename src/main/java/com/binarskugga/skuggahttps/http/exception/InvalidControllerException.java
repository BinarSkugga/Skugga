package com.binarskugga.skuggahttps.http.exception;

import static com.binarskugga.skuggahttps.response.Response.*;

public class InvalidControllerException extends HTTPException {

	public InvalidControllerException(String message) {
		super(INTERNAL_ERROR, message);
	}

}

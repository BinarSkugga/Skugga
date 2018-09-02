package com.binarskugga.skuggahttps.http.exception;

import static com.binarskugga.skuggahttps.http.Response.*;

public class AllowWithoutTargetException extends HTTPException {

	public AllowWithoutTargetException(String message) {
		super(INTERNAL_ERROR, message);
	}

	public AllowWithoutTargetException() {
		super(INTERNAL_ERROR);
	}
}

package com.binarskugga.skuggahttps.http.exception;

import static com.binarskugga.skuggahttps.http.Response.*;

public class InvalidSubjectException extends HTTPException {

	public InvalidSubjectException(String message) {
		super(INTERNAL_ERROR, message);
	}

	public InvalidSubjectException() {
		super(INTERNAL_ERROR);
	}

}

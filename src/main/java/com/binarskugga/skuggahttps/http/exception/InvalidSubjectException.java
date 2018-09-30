package com.binarskugga.skuggahttps.http.exception;

import static com.binarskugga.skuggahttps.response.Response.*;

public class InvalidSubjectException extends HTTPException {

	public InvalidSubjectException(String message) {
		super(INTERNAL_ERROR, message);
	}

}

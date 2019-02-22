package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class InternalServerException extends HttpException {

	public InternalServerException() {
		super(HttpStatus.INTERNAL_ERROR, HttpStatus.INTERNAL_ERROR.getCaption());
	}

	public InternalServerException(String message) {
		super(HttpStatus.INTERNAL_ERROR, message);
	}

}

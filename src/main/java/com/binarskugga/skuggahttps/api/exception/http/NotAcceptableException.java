package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class NotAcceptableException extends HttpException {

	public NotAcceptableException() {
		super(HttpStatus.NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE.getCaption());
	}

	public NotAcceptableException(String message) {
		super(HttpStatus.NOT_ACCEPTABLE, message);
	}

}

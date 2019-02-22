package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class GoneException extends HttpException {

	public GoneException(String message) {
		super(HttpStatus.GONE, message);
	}

	public GoneException() {
		super(HttpStatus.GONE, HttpStatus.GONE.getCaption());
	}

}

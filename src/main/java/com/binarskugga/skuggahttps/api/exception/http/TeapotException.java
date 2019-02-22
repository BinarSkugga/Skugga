package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class TeapotException extends HttpException {

	public TeapotException() {
		super(HttpStatus.TEAPOT, HttpStatus.TEAPOT.getCaption());
	}

	public TeapotException(String message) {
		super(HttpStatus.TEAPOT, message);
	}

}

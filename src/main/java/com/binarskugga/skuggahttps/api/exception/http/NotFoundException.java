package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class NotFoundException extends HttpException {

	public NotFoundException() {
		super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getCaption());
	}

	public NotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

}

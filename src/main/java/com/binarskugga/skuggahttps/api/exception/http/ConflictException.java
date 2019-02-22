package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class ConflictException extends HttpException {

	public ConflictException(String message) {
		super(HttpStatus.CONFLICT, message);
	}

	public ConflictException() {
		super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getCaption());
	}

}

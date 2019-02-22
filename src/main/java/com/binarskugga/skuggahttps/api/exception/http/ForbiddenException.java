package com.binarskugga.skuggahttps.api.exception.http;

import com.binarskugga.skuggahttps.api.enums.HttpStatus;

public class ForbiddenException extends HttpException {

	public ForbiddenException(String message) {
		super(HttpStatus.FORBIDDEN, message);
	}

	public ForbiddenException() {
		super(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getCaption());
	}

}
